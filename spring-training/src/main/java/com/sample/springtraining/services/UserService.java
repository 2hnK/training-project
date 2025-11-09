package com.sample.springtraining.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.sample.springtraining.models.QUser;
import com.sample.springtraining.models.User;
import com.sample.springtraining.projections.NameOnly;
import com.sample.springtraining.repositories.UserRepository;

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

    public List<User> findAllUsers() {
        /* 모든 사용자 조회 */

        Iterable<User> iterable = userRepository.findAll(Sort.unsorted());
        List<User> users = new ArrayList<>();

        for (User user : iterable) {
            users.add(user);
        }
        return users;
    }

    public User findUserByNickname(String nickname) {
        /* 닉네임이 {nickname}인 사용자 조회 */

        QUser user = QUser.user;
        JPAQuery<User> query = new JPAQuery<>(entityManager);

        return query.select(user)
                .from(user)
                .where(user.nickname.eq(nickname))
                .fetchOne();
    }

    public List<User> findUsersByEmailContaining(String query) {
        /* 이메일에 {query}가 포함된 사용자 조회 */

        if (!StringUtils.hasText(query)) {
            throw new IllegalArgumentException("쿼리를 입력해주세요");
        }

        QUser user = QUser.user;
        OrderSpecifier<String> ascOrder = user.email.asc();
        Iterable<User> iterable = userRepository.findAll(user.email.containsIgnoreCase(query.trim()), ascOrder);
        return StreamSupport.stream(iterable.spliterator(), false)
                .toList();
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
