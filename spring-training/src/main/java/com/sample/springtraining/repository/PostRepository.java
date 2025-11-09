package com.sample.springtraining.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sample.springtraining.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long>{
    
}
