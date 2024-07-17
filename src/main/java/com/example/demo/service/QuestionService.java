package com.example.demo.service;

import com.example.demo.DataNotFoundException;
import com.example.demo.entity.Answer;
import com.example.demo.entity.Question;
import com.example.demo.entity.SiteUser;
import com.example.demo.repository.QuestionRepository;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    private Specification<Question> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
            }
        };
    }

    public Question getQuestion(Long id) {
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent())
            return question.get();
        else
            throw new DataNotFoundException("question not found");
    }

    public void create(String subject, String content, SiteUser siteUser) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setAuthor(siteUser);
        this.questionRepository.save(q);
    }

    public Page<Question> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>(); // Sort.Order라는 정렬 객체 생성
        sorts.add(Sort.Order.desc("createDate")); // 정렬객체 삽입
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts)); // 정렬객체 넣어서 반환
        Specification<Question> spec = search(kw);
        return this.questionRepository.findAllByKeyword(kw, pageable);
    }

    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        this.questionRepository.save(question);
    }

    public void delete(Question question) {
        this.questionRepository.delete(question);
    }

    public void vote(Question question, SiteUser siteUser) {
        question.getVoter().add(siteUser);
        this.questionRepository.save(question);
    }
}
