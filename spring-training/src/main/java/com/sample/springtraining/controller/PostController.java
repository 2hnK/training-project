package com.sample.springtraining.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sample.springtraining.dto.common.ApiResponse;
import com.sample.springtraining.dto.post.PostCreateRequest;
import com.sample.springtraining.dto.post.PostResponse;
import com.sample.springtraining.service.PostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> getPost(@PathVariable Long id) {
        PostResponse post = postService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(post));
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<Page<PostResponse>>> getPosts() {
        Page<PostResponse> posts = postService.findAll();
        return ResponseEntity.ok(ApiResponse.success(posts));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PostResponse>> createPost(
            @Valid @RequestBody PostCreateRequest request) {

        PostResponse response = postService.createPost(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("게시물이 생성되었습니다.", response));
    }
}
