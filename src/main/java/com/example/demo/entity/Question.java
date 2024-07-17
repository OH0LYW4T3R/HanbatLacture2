package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Question {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 200)
    private String subject;
    @Column(columnDefinition = "TEXT")
    private String content;
    @CreationTimestamp
    private LocalDateTime createDate;
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;
    @ManyToOne
    private SiteUser author;
    private LocalDateTime modifyDate;
    @ManyToMany
    Set<SiteUser> voter;
}

