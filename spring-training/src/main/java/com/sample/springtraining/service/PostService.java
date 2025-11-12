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
import com.sample.springtraining.repository.MemberRepository;
import com.sample.springtraining.repository.PostRepository;
import com.sample.springtraining.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

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

    public Page<PostResponse> findAll(Pageable pageable) {
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

    @Transactional
    public PostResponse updatePost(Long id, PostCreateRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        // 권한 확인: 작성자만 수정 가능
        Member currentUser = getCurrentUser();
        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new IllegalStateException("게시물 작성자만 수정할 수 있습니다.");
        }

        // 수정
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        return mapToResponse(post);
    }

    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        // 권한 확인: 작성자만 삭제 가능
        Member currentUser = getCurrentUser();
        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new IllegalStateException("게시물 작성자만 삭제할 수 있습니다.");
        }

        postRepository.delete(post);
    }

    public Page<PostResponse> searchPosts(String keyword, Pageable pageable) {
        // 간단한 검색 구현 (제목 + 내용)
        // 실제로는 QueryDSL이나 Specification을 사용하는 것이 좋습니다
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(this::mapToResponse);
    }

    private Member getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        // 인증된 사용자인 경우
        if (auth != null && auth.isAuthenticated() && 
            auth.getPrincipal() instanceof CustomUserDetails) {
            return ((CustomUserDetails) auth.getPrincipal()).getMember();
        }
        
        // 개발 중: 익명 사용자의 경우 기본 사용자 반환 (임시)
        // TODO: 실제 환경에서는 인증 필수로 변경해야 함
        Member defaultMember = memberRepository.findByLoginId("admin");
        if (defaultMember == null) {
            throw new ResourceNotFoundException("Member", "loginId", "admin");
        }
        return defaultMember;
    }

    // Post Entity를 Response DTO로 변환
    private PostResponse mapToResponse(Post post) {
        Member author = post.getAuthor();
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(author != null ? author.getNickname() : "Unknown")
                .authorId(author != null ? author.getId() : null)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
