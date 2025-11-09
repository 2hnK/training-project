package com.sample.springtraining.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.sample.springtraining.models.User;
import com.sample.springtraining.projections.NameOnly;

public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {

    Iterable<NameOnly> findNameOnlyByEmail(String email);

    User findByLoginId(String loginId);

}
