package com.sample.springtraining.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sample.springtraining.dto.course.CourseCreateRequest;
import com.sample.springtraining.models.Course;
import com.sample.springtraining.repositories.CourseRepository;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final Timer createCourseTimer;
    private final Counter successCounter;
    private final Counter failureCounter;
    private final DistributionSummary createDistributionSummary;

    public CourseService(CourseRepository courseRepository,
            Timer createCourseTimer,
            @Qualifier("successCounter") Counter successCounter,
            @Qualifier("failureCounter") Counter failureCounter,
            DistributionSummary createDistributionSummary) {
        this.courseRepository = courseRepository;
        this.createCourseTimer = createCourseTimer;
        this.successCounter = successCounter;
        this.failureCounter = failureCounter;
        this.createDistributionSummary = createDistributionSummary;
    }

    public Course createCourse(CourseCreateRequest request) {
        return createCourseTimer.record(() -> {
            try {
                Course savedCourse = courseRepository.save(request.toEntity());
                successCounter.increment();

                // 평점 분포 기록 (DistributionSummary)
                createDistributionSummary.record(savedCourse.getRating());

                log.debug("Course created successfully: {} with rating: {}",
                        savedCourse.getId(), savedCourse.getRating());
                return savedCourse;
            } catch (Exception e) {
                failureCounter.increment();
                log.error("Failed to create course", e);
                throw e;
            }
        });
    }

    public long count() {
        return courseRepository.count();
    }

    // @SneakyThrows
    // public Course createCourse(Course course) {
    // createDistributionSummary.record(course.getRating());
    // return createCourseTimer.recordCallable(() -> courseRepository.save(course));
    // }
}
