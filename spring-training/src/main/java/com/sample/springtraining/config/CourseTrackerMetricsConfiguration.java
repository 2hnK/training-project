package com.sample.springtraining.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sample.springtraining.services.CourseService;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

@Configuration
public class CourseTrackerMetricsConfiguration {

    @Bean
    public Timer createCoursesTimer(MeterRegistry meterRegistry) {
        return Timer.builder("api.courses.creation.time")
                .description("Course creation execution time")
                .tags("operation", "create")
                .register(meterRegistry);
    }

    @Bean
    public Counter successCounter(MeterRegistry meterRegistry) {
        return Counter.builder("api.courses.created.success")
                .description("Number of successfully created courses")
                .tags("status", "success", "operation", "create")
                .register(meterRegistry);
    }

    @Bean
    public Counter failureCounter(MeterRegistry meterRegistry) {
        return Counter.builder("api.courses.created.failure")
                .description("Number of failed course creation attempts")
                .tags("status", "failure", "operation", "create")
                .register(meterRegistry);
    }

    @Bean
    public Gauge createCoursesGauge(MeterRegistry meterRegistry, CourseService courseService) {
        return Gauge.builder("api.courses.total.count", courseService, CourseService::count)
                .description("Current total number of courses in database")
                .tag("type", "snapshot")
                .register(meterRegistry);
    }

    @Bean
    public DistributionSummary createDistributionSummary(MeterRegistry meterRegistry) {
        return DistributionSummary.builder("api.courses.rating.distribution")
                .description("Course rating distribution statistics")
                .baseUnit("rating")
                .tags("metric_type", "quality")
                .register(meterRegistry);
    }
}
