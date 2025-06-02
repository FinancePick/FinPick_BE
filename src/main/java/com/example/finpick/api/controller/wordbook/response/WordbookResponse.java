package com.example.finpick.api.controller.wordbook.response;

import com.example.finpick.domain.wordbook.Wordbook;
import com.example.finpick.domain.wordbook.WordbookWord;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class WordbookResponse {
    private final Long id;
    private final String name;
    private final String description;
    private final String ownerName;
    private final boolean isPublic;
    private final List<WordResponse> words;

    public WordbookResponse(Wordbook wordbook, List<WordbookWord> wordbookWords) {
        this.id = wordbook.getId();
        this.name = wordbook.getName();
        this.description = wordbook.getDescription();
        this.ownerName = wordbook.getUser().getUsername();
        this.isPublic = wordbook.isPublic();
        this.words = wordbookWords.stream()
                .map(ww -> new WordResponse(ww.getWord()))
                .collect(Collectors.toList());
    }
} 