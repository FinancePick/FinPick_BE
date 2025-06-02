package com.example.finpick.api.controller.wordbook.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class WordbookRequest {

    @NotBlank(message = "단어장 이름은 필수입니다.")
    @Size(max = 100, message = "단어장 이름은 100자를 초과할 수 없습니다.")
    private String name;

    @Size(max = 1000, message = "설명은 1000자를 초과할 수 없습니다.")
    private String description;

    private boolean isPublic;
} 