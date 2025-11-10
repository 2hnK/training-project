package com.sample.springtraining.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.sample.springtraining.entity.Member;
import com.sample.springtraining.projection.NameOnly;

public interface MemberRepository extends JpaRepository<Member, Long>, QuerydslPredicateExecutor<Member> {

    Iterable<NameOnly> findNameOnlyByEmail(String email);

    Member findByLoginId(String loginId);

}
