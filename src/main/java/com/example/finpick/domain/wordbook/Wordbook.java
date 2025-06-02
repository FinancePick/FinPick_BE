package com.example.finpick.domain.wordbook;

import com.example.finpick.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "wordbook")
public class Wordbook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private boolean isPublic;

    public Wordbook() {
    }

    public Wordbook(String name, String description, User user, boolean isPublic) {
        this.name = name;
        this.description = description;
        this.user = user;
        this.isPublic = isPublic;
    }

    public void update(String name, String description, boolean isPublic) {
        this.name = name;
        this.description = description;
        this.isPublic = isPublic;
    }
} 