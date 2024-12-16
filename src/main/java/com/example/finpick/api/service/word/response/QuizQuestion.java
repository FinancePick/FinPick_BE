package com.example.finpick.api.service.word.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class QuizQuestion {
    private String meaning;      // 뜻
    private String correctOption; // 정답 옵션
    private List<String> Options; // 보기(4개)
}
