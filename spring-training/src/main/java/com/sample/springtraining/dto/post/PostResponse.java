package com.sample.springtraining.dto.post;

import java.time.LocalDateTime;

import com.sample.springtraining.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private Member author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
