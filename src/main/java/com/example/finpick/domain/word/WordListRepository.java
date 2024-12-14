package com.example.finpick.domain.word;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordListRepository extends JpaRepository<WordList, Long> {
    List<WordList> findByIdBetween(int startId, int endId);
}
