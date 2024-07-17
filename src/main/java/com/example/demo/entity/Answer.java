package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String content;
    @CreationTimestamp
    private LocalDateTime createDate;
    @ManyToOne
    private Question question;
    @ManyToOne
    private SiteUser author;
    private LocalDateTime modifyDate;
    @ManyToMany
    Set<SiteUser> voter;
}
