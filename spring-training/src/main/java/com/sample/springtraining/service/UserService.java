package com.sample.springtraining.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.sample.springtraining.entity.Member;
import com.sample.springtraining.projection.NameOnly;
import com.sample.springtraining.repository.UserRepository;

import jakarta.persistence.EntityManager;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @Autowired
    public UserService(UserRepository userRepository, EntityManager entityManager) {
        this.userRepository = userRepository;
        this.entityManager = entityManager;
    }

    public List<Member> findAllUsers() {
        /* 모든 사용자 조회 */

        Iterable<Member> iterable = userRepository.findAll(Sort.unsorted());
        List<Member> members = new ArrayList<>();

        for (Member member : iterable) {
            members.add(member);
        }
        return members;
    }

    public Member findUserByNickname(String nickname) {
        /* 닉네임이 {nickname}인 사용자 조회 */

        return entityManager.createQuery(
                "SELECT m FROM Member m WHERE m.nickname = :nickname", Member.class)
                .setParameter("nickname", nickname)
                .getSingleResult();
    }

    public List<Member> findUsersByEmailContaining(String query) {
        /* 이메일에 {query}가 포함된 사용자 조회 */

        if (!StringUtils.hasText(query)) {
            throw new IllegalArgumentException("쿼리를 입력해주세요");
        }

        return entityManager.createQuery(
                "SELECT m FROM Member m WHERE LOWER(m.email) LIKE LOWER(:query) ORDER BY m.email ASC", Member.class)
                .setParameter("query", "%" + query.trim() + "%")
                .getResultList();
    }

    public List<NameOnly> findNameOnlyByEmail(String email) {
        /* 이메일에 {email}이 포함된 사용자 이름 조회 */

        if (!StringUtils.hasText(email)) {
            throw new IllegalArgumentException("이메일을 입력해주세요");
        }

        Iterable<NameOnly> iterable = userRepository.findNameOnlyByEmail(email.trim());
        return StreamSupport.stream(iterable.spliterator(), false)
                .toList();
    }
}
