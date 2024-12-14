package com.example.finpick.api.controller.QuizController;

import com.example.finpick.domain.word.WordListRepository;
import com.example.finpick.domain.word.WordList;
import com.example.finpick.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class QuizController {

    @Autowired
    private WordListRepository wordListRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/quiz")
    public QuizResult handleQuiz(@RequestHeader("Authorization") String authorizationHeader,
                                 @RequestBody QuizRequest request) {
        String token = authorizationHeader.replace("Bearer ", "");
        String level = jwtUtil.getLevelFromToken(token);

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

        // 랜덤 퀴즈 단어 가져오기
        List<WordList> wordPool = wordListRepository.findByIdBetween(startId, endId);
        Collections.shuffle(wordPool);
        List<WordList> quizWords = wordPool.stream().limit(10).collect(Collectors.toList());

        // 정답 mapping 만들기
        Map<Long, String> correctAnswers = quizWords.stream()
                .collect(Collectors.toMap(WordList::getId, WordList::getWord));

        // 사용자 답안 확인
        List<Integer> incorrectQuestions = new ArrayList<>();
        int correctCount = 0;

        for (int i = 0; i < request.getAnswers().size(); i++) {
            QuizAnswer answer = request.getAnswers().get(i);
            String correctWord = correctAnswers.get(answer.getQuestionId());
            if (correctWord != null && correctWord.equals(answer.getSelectedAnswer())) {
                correctCount++;
            } else {
                incorrectQuestions.add(i + 1);
            }
        }

        // 사용자 레벨업 여부 확인
        boolean levelUp = correctCount >= 8;

        return new QuizResult(levelUp, incorrectQuestions);
    }

    // DTO
    public static class QuizAnswer {
        private Long questionId;
        private String selectedAnswer;

        // Getters and setters
        public Long getQuestionId() {
            return questionId;
        }

        public void setQuestionId(Long questionId) {
            this.questionId = questionId;
        }

        public String getSelectedAnswer() {
            return selectedAnswer;
        }

        public void setSelectedAnswer(String selectedAnswer) {
            this.selectedAnswer = selectedAnswer;
        }
    }

    public static class QuizResult {
        private boolean levelUp;
        private List<Integer> incorrectQuestions;

        public QuizResult(boolean levelUp, List<Integer> incorrectQuestions) {
            this.levelUp = levelUp;
            this.incorrectQuestions = incorrectQuestions;
        }

        public boolean isLevelUp() {
            return levelUp;
        }

        public List<Integer> getIncorrectQuestions() {
            return incorrectQuestions;
        }
    }

    public static class QuizRequest {
        private List<QuizAnswer> answers;

        public List<QuizAnswer> getAnswers() {
            return answers;
        }

        public void setAnswers(List<QuizAnswer> answers) {
            this.answers = answers;
        }
    }
}