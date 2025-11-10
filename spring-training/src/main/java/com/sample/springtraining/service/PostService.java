package com.sample.springtraining.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sample.springtraining.dto.post.PostCreateRequest;
import com.sample.springtraining.dto.post.PostResponse;
import com.sample.springtraining.entity.Member;
import com.sample.springtraining.entity.Post;
import com.sample.springtraining.exception.ResourceNotFoundException;
import com.sample.springtraining.repository.PostRepository;
import com.sample.springtraining.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;

    public PostResponse findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        return mapToResponse(post);
    }

    public Page<PostResponse> findAll() {
        Pageable pageable = PageRequest.of(0, 10); // 기본값: 첫 페이지, 10개씩
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(this::mapToResponse);
    }

    @Transactional
    public PostResponse createPost(PostCreateRequest request) {
        Member currentUser = getCurrentUser();

        Post post = request.toEntity();
        post.setAuthor(currentUser);

        Post savedPost = postRepository.save(post);
        return mapToResponse(savedPost);
    }

    private Member getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ((CustomUserDetails) auth.getPrincipal()).getMember();
    }

    // Post Entity를 Response DTO로 변환
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
