package com.example.finpick.api.service.word;

import com.example.finpick.domain.user.User;
import com.example.finpick.domain.user.UserRepository;
import com.example.finpick.domain.word.Word;
import com.example.finpick.domain.word.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;
    private final UserRepository userRepository;

    public Word getTodayWord(String username) {
        // 사용자 정보 가져오기
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 사용자 레벨 가져오기 (Beginner, Intermediate 등)
        String userLevel = user.getLevel();

        // 해당 레벨의 단어 목록 조회
        List<Word> words = wordRepository.findByLevel(userLevel);

        if (words.isEmpty()) {
            throw new IllegalArgumentException("No words available for this level.");
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
}
