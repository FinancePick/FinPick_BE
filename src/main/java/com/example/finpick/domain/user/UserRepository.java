package com.example.finpick.domain.user;

import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    // 특정 레벨의 사용자 조회 메서드 추가 가능
    Optional<User> findByUsernameAndLevel(String username, String level);
}