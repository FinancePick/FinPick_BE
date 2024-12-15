package com.example.finpick.api.controller.word;

import com.example.finpick.api.service.word.WordService;
import com.example.finpick.domain.word.Word;
import com.example.finpick.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/word")
@RequiredArgsConstructor
public class WordController {

    private final WordService wordService;
    private final JwtUtil jwtUtil;

    @GetMapping("/today")
    public ResponseEntity<Word> getTodayWord(@RequestHeader("Authorization") String token) {
        // 토큰에서 사용자 이름 추출
        String username = jwtUtil.extractUsername(token.replace("Bearer ", ""));

        // 사용자 레벨에 맞는 오늘의 단어 반환
        Word todayWord = wordService.getTodayWord(username);
        return ResponseEntity.ok(todayWord);
    }
}
