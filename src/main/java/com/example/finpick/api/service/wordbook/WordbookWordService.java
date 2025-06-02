package com.example.finpick.api.service.wordbook;

import com.example.finpick.domain.user.User;
import com.example.finpick.domain.word.Word;
import com.example.finpick.domain.wordbook.Wordbook;
import com.example.finpick.domain.wordbook.WordbookWord;
import com.example.finpick.domain.wordbook.WordbookWordRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WordbookWordService {

    private final WordbookService wordbookService;
    private final WordbookWordRepository wordbookWordRepository;

    @Transactional
    public void addWordToWordbook(Long wordbookId, Word word, User user) {
        Wordbook wordbook = wordbookService.getWordbookById(wordbookId);

        if (!wordbook.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("단어장에 단어를 추가할 권한이 없습니다.");
        }

        if (wordbookWordRepository.existsByWordbookAndWord(wordbook, word)) {
            throw new IllegalStateException("이미 단어장에 추가된 단어입니다.");
        }

        WordbookWord wordbookWord = new WordbookWord(wordbook, word);
        wordbookWordRepository.save(wordbookWord);
    }

    public List<WordbookWord> getWordsInWordbook(Long wordbookId, User user) {
        Wordbook wordbook = wordbookService.getWordbookById(wordbookId);
        
        if (!wordbook.isPublic() && !wordbook.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("단어장을 조회할 권한이 없습니다.");
        }

        return wordbookWordRepository.findByWordbook(wordbook);
    }

    @Transactional
    public void removeWordFromWordbook(Long wordbookId, Word word, User user) {
        Wordbook wordbook = wordbookService.getWordbookById(wordbookId);

        if (!wordbook.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("단어장에서 단어를 제거할 권한이 없습니다.");
        }

        WordbookWord wordbookWord = wordbookWordRepository.findByWordbookAndWord(wordbook, word)
                .orElseThrow(() -> new EntityNotFoundException("단어장에 해당 단어가 존재하지 않습니다."));

        wordbookWordRepository.delete(wordbookWord);
    }
} 