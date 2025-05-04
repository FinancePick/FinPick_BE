package com.example.finpick.api.service.word;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.finpick.api.service.word.response.QuizQuestion;
import com.example.finpick.domain.user.User;
import com.example.finpick.domain.user.UserRepository;
import com.example.finpick.domain.word.Word;
import com.example.finpick.domain.word.WordRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;
    private final UserRepository userRepository;

    public Word getTodayWord(String username) {
        // 사용자 정보 가져오기
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 모든 단어 목록 조회
        List<Word> words = wordRepository.findAll();

        if (words.isEmpty()) {
            throw new IllegalArgumentException("No words available.");
        }

        // 날짜 기반 단어 인덱스 계산
        int todayIndex = calculateIndexForDate(LocalDate.now(), words.size());

        // 오늘의 단어 반환
        return words.get(todayIndex);
    }

    private int calculateIndexForDate(LocalDate date, int wordCount) {
        int dayOfYear = date.getDayOfYear(); // 오늘 날짜 값 (1 ~ 365)
        return dayOfYear % wordCount;       // 단어 개수로 나머지 계산
    }

    public List<QuizQuestion> generateQuiz(String username) {
        // 사용자 정보 가져오기
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 모든 단어 목록 가져오기
        List<Word> words = wordRepository.findAll();
        if (words.size() < 4) {
            throw new IllegalArgumentException("Not enough words available for quiz generation.");
        }

        // 퀴즈용 단어 10개 랜덤 선택
        Collections.shuffle(words);
        List<Word> selectedWords = words.subList(0, Math.min(10, words.size()));

        // 퀴즈 생성
        List<QuizQuestion> quiz = new ArrayList<>();
        Random random = new Random();

        for (Word correctWord : selectedWords) {
            HashSet<Word> options = new HashSet<>();
            options.add(correctWord);

            // 보기 추가 (정답 + 3개의 랜덤 단어)
            while (options.size() < 4) {
                Word randomWord = words.get(random.nextInt(words.size()));
                options.add(randomWord);
            }

            // 보기를 섞음
            List<String> choices = new ArrayList<>();
            for (Word option : options) {
                choices.add(option.getWord());
            }
            Collections.shuffle(choices);

            // 퀴즈 질문 추가
            quiz.add(new QuizQuestion(correctWord.getMeaning(), correctWord.getWord(), choices));
        }

        return quiz;
    }

    public Page<Word> getAllWords(String username, Pageable pageable) {
        //사용자 정보 가져오기
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return wordRepository.findAll(pageable);
    }
}
