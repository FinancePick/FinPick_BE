package com.example.finpick.api.controller.wordbook;

import com.example.finpick.api.controller.wordbook.request.WordbookRequest;
import com.example.finpick.api.controller.wordbook.response.WordbookResponse;
import com.example.finpick.api.service.wordbook.WordbookService;
import com.example.finpick.api.service.wordbook.WordbookWordService;
import com.example.finpick.config.CustomUserDetails;
import com.example.finpick.domain.word.Word;
import com.example.finpick.domain.word.WordRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wordbooks")
@RequiredArgsConstructor
public class WordbookController {

    private final WordbookService wordbookService;
    private final WordbookWordService wordbookWordService;
    private final WordRepository wordRepository;

    @PostMapping
    public ResponseEntity<WordbookResponse> createWordbook(
            @Valid @RequestBody WordbookRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(wordbookService.createWordbook(request, userDetails.getUser()));
    }

    @GetMapping
    public ResponseEntity<Page<WordbookResponse>> getWordbooks(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Pageable pageable) {
        return ResponseEntity.ok(wordbookService.getWordbooks(userDetails.getUser(), pageable));
    }

    @GetMapping("/my")
    public ResponseEntity<Page<WordbookResponse>> getMyWordbooks(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Pageable pageable) {
        return ResponseEntity.ok(wordbookService.getMyWordbooks(userDetails.getUser(), pageable));
    }

    @PutMapping("/{wordbookId}")
    public ResponseEntity<WordbookResponse> updateWordbook(
            @PathVariable Long wordbookId,
            @Valid @RequestBody WordbookRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(wordbookService.updateWordbook(wordbookId, request, userDetails.getUser()));
    }

    @DeleteMapping("/{wordbookId}")
    public ResponseEntity<Void> deleteWordbook(
            @PathVariable Long wordbookId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        wordbookService.deleteWordbook(wordbookId, userDetails.getUser());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{wordbookId}/words/{wordId}")
    public ResponseEntity<Void> addWordToWordbook(
            @PathVariable Long wordbookId,
            @PathVariable Long wordId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new IllegalArgumentException("단어를 찾을 수 없습니다."));
        wordbookWordService.addWordToWordbook(wordbookId, word, userDetails.getUser());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{wordbookId}/words/{wordId}")
    public ResponseEntity<Void> removeWordFromWordbook(
            @PathVariable Long wordbookId,
            @PathVariable Long wordId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new IllegalArgumentException("단어를 찾을 수 없습니다."));
        wordbookWordService.removeWordFromWordbook(wordbookId, word, userDetails.getUser());
        return ResponseEntity.ok().build();
    }
} 