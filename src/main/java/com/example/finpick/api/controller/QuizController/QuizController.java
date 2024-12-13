package com.example.finpick.api.controller.QuizController;

import com.example.finpick.domain.word.WordListRepository;
import com.example.finpick.domain.word.wordList;
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

    @PostMapping("/quiz")
    //public QuizResult handleQuiz(@RequestParam String level, @RequestBody List<QuizAnswer> answers) {
    public QuizResult handleQuiz(@RequestBody QuizRequest request) {
        // 사용자 레벨에 따라 id 범위 설정
        String level = request.getLevel();
        List<QuizAnswer> answers = request.getAnswers();
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

        // 퀴즈에 무작위 단어 10개 가져오기
        List<wordList> wordPool = wordListRepository.findByIdBetween(startId, endId);
        Collections.shuffle(wordPool);
        List<wordList> quizWords = wordPool.stream().limit(10).collect(Collectors.toList());

        // 정답 mapping 준비
        Map<Long, String> correctAnswers = quizWords.stream()
                .collect(Collectors.toMap(wordList::getId, wordList::getWord));

        // 사용자 답변 검증
        List<Integer> incorrectQuestions = new ArrayList<>();
        int correctCount = 0;

        for (int i = 0; i < answers.size(); i++) {
            QuizAnswer answer = answers.get(i);
            String correctWord = correctAnswers.get(answer.getQuestionId());
            if (correctWord != null && correctWord.equals(answer.getSelectedAnswer())) {
                correctCount++;
            } else {
                incorrectQuestions.add(i + 1); // 질문 번호는 1번 기반
            }
        }

        // 사용자 레벨 업 여부 확인
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

    //
    public static class QuizRequest {
        private String level;
        private List<QuizAnswer> answers;
        // Getters and setters
        public String getLevel() { return level; }
        public void setLevel(String level) { this.level = level; }
        public List<QuizAnswer> getAnswers() { return answers; }
        public void setAnswers(List<QuizAnswer> answers) { this.answers = answers; }
    }
}