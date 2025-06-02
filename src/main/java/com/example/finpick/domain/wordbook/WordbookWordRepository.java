package com.example.finpick.domain.wordbook;

import com.example.finpick.domain.word.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface WordbookWordRepository extends JpaRepository<WordbookWord, Long> {
    boolean existsByWordbookAndWord(Wordbook wordbook, Word word);
    Optional<WordbookWord> findByWordbookAndWord(Wordbook wordbook, Word word);
    List<WordbookWord> findByWordbook(Wordbook wordbook);

    @Modifying
    @Transactional
    @Query("DELETE FROM WordbookWord ww WHERE ww.wordbook = :wordbook")
    void deleteByWordbook(@Param("wordbook") Wordbook wordbook);
} 