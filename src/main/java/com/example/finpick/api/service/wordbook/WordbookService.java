package com.example.finpick.api.service.wordbook;

import com.example.finpick.api.controller.wordbook.request.WordbookRequest;
import com.example.finpick.api.controller.wordbook.response.WordbookResponse;
import com.example.finpick.domain.user.User;
import com.example.finpick.domain.wordbook.Wordbook;
import com.example.finpick.domain.wordbook.WordbookRepository;
import com.example.finpick.domain.wordbook.WordbookWord;
import com.example.finpick.domain.wordbook.WordbookWordRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WordbookService {

    private final WordbookRepository wordbookRepository;
    private final WordbookWordRepository wordbookWordRepository;

    public Wordbook getWordbookById(Long wordbookId) {
        return wordbookRepository.findById(wordbookId)
                .orElseThrow(() -> new EntityNotFoundException("단어장을 찾을 수 없습니다."));
    }

    @Transactional
    public WordbookResponse createWordbook(WordbookRequest request, User user) {
        Wordbook wordbook = new Wordbook(
                request.getName(),
                request.getDescription(),
                user,
                request.isPublic()
        );
        Wordbook saved = wordbookRepository.save(wordbook);
        List<WordbookWord> words = wordbookWordRepository.findByWordbook(saved);
        return new WordbookResponse(saved, words);
    }

    public Page<WordbookResponse> getWordbooks(User user, Pageable pageable) {
        Page<Wordbook> wordbookPage = wordbookRepository.findByIsPublicTrueOrUser(user, pageable);
        List<WordbookResponse> content = wordbookPage.getContent().stream()
                .map(wb -> new WordbookResponse(wb, wordbookWordRepository.findByWordbook(wb)))
                .collect(Collectors.toList());
        return new PageImpl<>(content, pageable, wordbookPage.getTotalElements());
    }

    public Page<WordbookResponse> getMyWordbooks(User user, Pageable pageable) {
        Page<Wordbook> wordbookPage = wordbookRepository.findByUser(user, pageable);
        List<WordbookResponse> content = wordbookPage.getContent().stream()
                .map(wb -> new WordbookResponse(wb, wordbookWordRepository.findByWordbook(wb)))
                .collect(Collectors.toList());
        return new PageImpl<>(content, pageable, wordbookPage.getTotalElements());
    }

    @Transactional
    public WordbookResponse updateWordbook(Long wordbookId, WordbookRequest request, User user) {
        Wordbook wordbook = getWordbookById(wordbookId);

        if (!wordbook.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("단어장을 수정할 권한이 없습니다.");
        }

        wordbook.update(request.getName(), request.getDescription(), request.isPublic());
        List<WordbookWord> words = wordbookWordRepository.findByWordbook(wordbook);
        return new WordbookResponse(wordbook, words);
    }

    @Transactional
    public void deleteWordbook(Long wordbookId, User user) {
        Wordbook wordbook = getWordbookById(wordbookId);

        if (!wordbook.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("단어장을 삭제할 권한이 없습니다.");
        }

        wordbookWordRepository.deleteByWordbook(wordbook);
        wordbookRepository.delete(wordbook);
    }
} 