package com.example.finpick.api.controller.WordController;

import com.example.finpick.domain.word.WordListRepository;
import com.example.finpick.domain.word.wordList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class WordController {

    @Autowired
    private WordListRepository wordListRepository;

    @GetMapping("/daily-word")
    public WordResponse getDailyWord(@RequestParam String username, @RequestParam String password, @RequestParam String level) {
        // 사용자 레벨에 따라 id 범위 설정
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

        // 날짜를 기준으로 오늘의 index 계산
        LocalDate today = LocalDate.now();
        int dayOfMonth = today.getDayOfMonth();
        int range = endId - startId + 1;
        int todayIndex = (dayOfMonth - 1) % range + startId;

        // 데이터베이스에서 단어 가져오기
        Optional<wordList> optionalWord = wordListRepository.findById((long) todayIndex);
        if (optionalWord.isPresent()) {
            wordList word = optionalWord.get();
            return new WordResponse(level, word.getWord(), word.getDescription());
        } else {
            throw new RuntimeException("Word not found for ID: " + todayIndex);
        }
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

