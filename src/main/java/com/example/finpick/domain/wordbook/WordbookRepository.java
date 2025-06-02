package com.example.finpick.domain.wordbook;

import com.example.finpick.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WordbookRepository extends JpaRepository<Wordbook, Long> {
    
    Page<Wordbook> findByUser(User user, Pageable pageable);
    
    @Query("SELECT w FROM Wordbook w WHERE w.isPublic = true OR w.user = :user")
    Page<Wordbook> findByIsPublicTrueOrUser(@Param("user") User user, Pageable pageable);
    
    boolean existsByNameAndUser(String name, User user);
} 