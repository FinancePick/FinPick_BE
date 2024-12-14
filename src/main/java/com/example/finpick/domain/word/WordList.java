package com.example.finpick.domain.word;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "quizlist")
public class WordList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 단어 ID

    @Column(nullable = false)
    private String word; // 단어

    @Column(columnDefinition = "TEXT")
    private String description; // 단어 설명

    @Column(nullable = false)
    private String level; // 단어 레벨

}
