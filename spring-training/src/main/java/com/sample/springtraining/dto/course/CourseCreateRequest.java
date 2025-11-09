package com.sample.springtraining.dto.course;

import com.sample.springtraining.models.Course;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CourseCreateRequest(
        @NotBlank(message = "코스 이름은 필수입니다.") String name,
        @NotBlank(message = "카테고리는 필수입니다.") String category,
        @NotNull(message = "평점은 필수입니다.") @Min(value = 0, message = "평점은 0 이상이어야 합니다.") @Max(value = 5, message = "평점은 5 이하여야 합니다.") Integer rating,
        @NotBlank(message = "설명은 필수입니다.") String description) {

    public Course toEntity() {
        return Course.builder()
                .name(name)
                .category(category)
                .rating(rating)
                .description(description)
                .build();
    }
}
