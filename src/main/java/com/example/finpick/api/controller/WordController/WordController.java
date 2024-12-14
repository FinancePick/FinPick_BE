package com.example.finpick.api.controller.WordController;

import com.example.finpick.domain.word.WordListRepository;
import com.example.finpick.domain.word.WordList;
import com.example.finpick.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class WordController {

    @Autowired
    private WordListRepository wordListRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/daily-word")
    public WordResponse getDailyWord(@RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.replace("Bearer ", "");

        String level;
        try {
            level = jwtUtil.getLevelFromToken(token);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JWT token", e);
        }

        int startId, endId;
        switch (level.toLowerCase()) {
            case "beginner":
                startId = 1;
                endId = 25;
                break;
            case "medium":
                startId = 26;
                endId = 50;
                break;
            case "advanced":
                startId = 51;
                endId = 75;
                break;
            case "professional":
                startId = 76;
                endId = 100;
                break;
            default:
                throw new IllegalArgumentException("Invalid level: " + level);
        }

        LocalDate today = LocalDate.now();
        int dayOfMonth = today.getDayOfMonth();
        int range = endId - startId + 1;
        int todayIndex = (dayOfMonth - 1) % range + startId;

        Optional<WordList> optionalWord = wordListRepository.findById((long) todayIndex);
        if (optionalWord.isEmpty()) {
            throw new IllegalStateException("Word not found for ID: " + todayIndex);
        }

        WordList word = optionalWord.get();
        return new WordResponse(level, word.getWord(), word.getDescription());
    }

    // DTO for response
    public static class WordResponse {
        private String level;
        private String word;
        private String description;

        public WordResponse(String level, String word, String description) {
            this.level = level;
            this.word = word;
            this.description = description;
        }

        public String getLevel() {
            return level;
        }

        public String getWord() {
            return word;
        }

        public String getDescription() {
            return description;
        }
    }
}

