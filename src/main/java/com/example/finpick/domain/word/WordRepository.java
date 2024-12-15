package com.example.finpick.domain.word;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WordRepository extends JpaRepository<Word, Long> {
    List<Word> findByLevel(String level); // 특정 레벨의 단어 조회
}
