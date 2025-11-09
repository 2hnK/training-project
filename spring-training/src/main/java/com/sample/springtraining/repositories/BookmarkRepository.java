package com.sample.springtraining.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sample.springtraining.models.Bookmark;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

}