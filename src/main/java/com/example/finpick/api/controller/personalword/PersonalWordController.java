package com.example.finpick.api.controller.personalword;

import com.example.finpick.api.service.personalword.PersonalWordService;
import com.example.finpick.domain.personalword.PersonalWord;
import com.example.finpick.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/personal-words")
@RequiredArgsConstructor
public class PersonalWordController {

    private final PersonalWordService personalWordService;
    private final JwtUtil jwtUtil;

    @PostMapping("/{wordId}")
    public ResponseEntity<PersonalWord> addWordToPersonalList(
            @RequestHeader("Authorization") String token,
            @PathVariable Long wordId) {
        String username = extractUsernameFromToken(token);
        PersonalWord personalWord = personalWordService.addWordToPersonalList(username, wordId);
        return ResponseEntity.ok(personalWord);
    }

    @DeleteMapping("/{wordId}")
    public ResponseEntity<Void> removeWordFromPersonalList(
            @RequestHeader("Authorization") String token,
            @PathVariable Long wordId) {
        String username = extractUsernameFromToken(token);
        personalWordService.removeWordFromPersonalList(username, wordId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<PersonalWord>> getPersonalWordList(
            @RequestHeader("Authorization") String token,
            Pageable pageable) {
        String username = extractUsernameFromToken(token);
        Page<PersonalWord> personalWords = personalWordService.getPersonalWordList(username, pageable);
        return ResponseEntity.ok(personalWords);
    }

    private String extractUsernameFromToken(String token) {
        // "Bearer " 접두사 제거
        String jwtToken = token.replace("Bearer ", "");
        return jwtUtil.extractUsername(jwtToken);
    }
} 