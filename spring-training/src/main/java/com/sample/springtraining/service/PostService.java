package com.sample.springtraining.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sample.springtraining.dto.post.PostResponse;
import com.sample.springtraining.entity.Post;
import com.sample.springtraining.exception.ResourceNotFoundException;
import com.sample.springtraining.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;

    /**
     * ID로 게시글 조회
     * @param id 게시글 ID
     * @return PostResponse DTO
     * @throws ResourceNotFoundException 게시글을 찾을 수 없는 경우
     */
    public PostResponse findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        
        return mapToResponse(post);
    }
    
    /**
     * Entity를 Response DTO로 변환
     * Single Responsibility Principle 적용 - 변환 로직 분리
     */
    private PostResponse mapToResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getAuthor())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
