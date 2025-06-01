package com.example.finpick.domain.personalword;

import com.example.finpick.domain.user.User;
import com.example.finpick.domain.word.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonalWordRepository extends JpaRepository<PersonalWord, Long> {
    Optional<PersonalWord> findByUserAndWord(User user, Word word);
    void deleteByUserAndWord(User user, Word word);
    Page<PersonalWord> findByUser(User user, Pageable pageable);
} 