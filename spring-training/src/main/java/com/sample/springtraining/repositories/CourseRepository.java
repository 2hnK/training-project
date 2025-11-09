package com.sample.springtraining.repositories;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sample.springtraining.models.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Iterable<Course> findAllByCategory(String category);

    Iterable<Course> findAllByCategoryOrderByName(String category);

    boolean existsByName(String name);

    long countByCategory(String category);

    Iterable<Course> findByNameOrCategory(String name, String category);

    Iterable<Course> findByNameStartsWith(String name);

    Stream<Course> streamAllByCategory(String category);
}