package com.example.finpick.api.service.personalword;

import com.example.finpick.domain.personalword.PersonalWord;
import com.example.finpick.domain.personalword.PersonalWordRepository;
import com.example.finpick.domain.user.User;
import com.example.finpick.domain.user.UserRepository;
import com.example.finpick.domain.word.Word;
import com.example.finpick.domain.word.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PersonalWordService {

    private final PersonalWordRepository personalWordRepository;
    private final UserRepository userRepository;
    private final WordRepository wordRepository;

    @Transactional
    public PersonalWord addWordToPersonalList(String username, Long wordId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new IllegalArgumentException("Word not found"));

        // 이미 추가된 단어인지 확인
        if (personalWordRepository.findByUserAndWord(user, word).isPresent()) {
            throw new IllegalArgumentException("Word already exists in personal list");
        }

        PersonalWord personalWord = new PersonalWord();
        personalWord.setUser(user);
        personalWord.setWord(word);

        return personalWordRepository.save(personalWord);
    }

    @Transactional
    public void removeWordFromPersonalList(String username, Long wordId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new IllegalArgumentException("Word not found"));

        personalWordRepository.deleteByUserAndWord(user, word);
    }

    @Transactional(readOnly = true)
    public Page<PersonalWord> getPersonalWordList(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        return personalWordRepository.findByUser(user, pageable);
    }
} 