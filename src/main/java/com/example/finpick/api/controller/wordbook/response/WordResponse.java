package com.example.finpick.api.controller.wordbook.response;

import com.example.finpick.domain.word.Word;
import lombok.Getter;

@Getter
public class WordResponse {
    private final Long id;
    private final String word;
    private final String meaning;

    public WordResponse(Word word) {
        this.id = word.getId();
        this.word = word.getWord();
        this.meaning = word.getMeaning();
    }
} 