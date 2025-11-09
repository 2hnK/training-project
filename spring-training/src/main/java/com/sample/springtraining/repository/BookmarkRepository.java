package com.sample.springtraining.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sample.springtraining.entity.Bookmark;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

}