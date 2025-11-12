package com.sample.springtraining.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sample.springtraining.dto.common.SuccessResponse;
import com.sample.springtraining.dto.post.PostCreateRequest;
import com.sample.springtraining.dto.post.PostResponse;
import com.sample.springtraining.service.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * 개선된 게시물 컨트롤러
 * 
 * 대기업 표준 API 설계 적용:
 * 1. 성공 응답: SuccessResponse<T> 사용
 * 2. 에러 응답: GlobalExceptionHandler가 ErrorResponse로 자동 변환
 * 3. 페이징: Meta 정보 포함
 * 4. HTTP 상태 코드: RESTful 원칙 준수
 * 5. API 문서화: Swagger/OpenAPI 어노테이션
 */
@Tag(name = "Posts", description = "게시물 관리 API")
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class ImprovedPostController {

    private final PostService postService;

    /**
     * 게시물 단건 조회
     * 
     * 성공: 200 OK + SuccessResponse<PostResponse>
     * 실패: 404 Not Found + ErrorResponse
     */
    @Operation(summary = "게시물 조회", description = "ID로 특정 게시물을 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<PostResponse>> getPost(
            @Parameter(description = "게시물 ID", example = "1")
            @PathVariable Long id) {
        
        PostResponse post = postService.findById(id);
        
        // 간단한 성공 응답 (data만 포함)
        return ResponseEntity.ok(SuccessResponse.of(post));
    }

    /**
     * 게시물 목록 조회 (페이징)
     * 
     * 대기업 표준: 페이징 메타데이터를 응답에 포함
     * - Google: nextPageToken
     * - Microsoft: @odata.nextLink
     * - 이 예제: meta 객체에 페이징 정보
     */
    @Operation(summary = "게시물 목록 조회", description = "페이징된 게시물 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<SuccessResponse<Page<PostResponse>>> getPosts(
            @Parameter(description = "페이지 번호 (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "페이지 크기", example = "20")
            @RequestParam(defaultValue = "20") int size,
            
            @Parameter(description = "정렬 기준", example = "createdAt")
            @RequestParam(defaultValue = "createdAt") String sortBy,
            
            @Parameter(description = "정렬 방향", example = "DESC")
            @RequestParam(defaultValue = "DESC") Sort.Direction direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<PostResponse> posts = postService.findAll(pageable);

        // 페이징 메타데이터 포함
        SuccessResponse<Page<PostResponse>> response = SuccessResponse.<Page<PostResponse>>builder()
                .data(posts)
                .meta(SuccessResponse.Meta.from(posts))
                .timestamp(java.time.LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * 게시물 생성
     * 
     * 성공: 201 Created + Location 헤더
     * 실패: 400 Bad Request (Validation 실패)
     */
    @Operation(summary = "게시물 생성", description = "새로운 게시물을 생성합니다.")
    @PostMapping
    public ResponseEntity<SuccessResponse<PostResponse>> createPost(
            @Valid @RequestBody PostCreateRequest request) {

        PostResponse response = postService.createPost(request);

        // 메시지를 포함한 성공 응답
        SuccessResponse<PostResponse> successResponse = 
                SuccessResponse.of(response, "게시물이 성공적으로 생성되었습니다.");

        // RESTful: 201 Created + Location 헤더
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", "/api/v1/posts/" + response.getId())
                .body(successResponse);
    }

    /**
     * 게시물 수정
     * 
     * 대기업 표준: PUT은 전체 교체, PATCH는 부분 수정
     */
    @Operation(summary = "게시물 수정", description = "기존 게시물을 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<PostResponse>> updatePost(
            @Parameter(description = "게시물 ID", example = "1")
            @PathVariable Long id,
            
            @Valid @RequestBody PostCreateRequest request) {

        PostResponse response = postService.updatePost(id, request);

        return ResponseEntity.ok(
                SuccessResponse.of(response, "게시물이 수정되었습니다.")
        );
    }

    /**
     * 게시물 삭제
     * 
     * 성공: 204 No Content (응답 본문 없음)
     * 대안: 200 OK + 삭제된 리소스 정보 반환
     */
    @Operation(summary = "게시물 삭제", description = "게시물을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @Parameter(description = "게시물 ID", example = "1")
            @PathVariable Long id) {

        postService.deletePost(id);

        // RESTful 표준: 204 No Content (본문 없음)
        return ResponseEntity.noContent().build();
    }

    /**
     * 게시물 검색
     * 
     * 쿼리 파라미터를 사용한 검색
     */
    @Operation(summary = "게시물 검색", description = "제목이나 내용으로 게시물을 검색합니다.")
    @GetMapping("/search")
    public ResponseEntity<SuccessResponse<Page<PostResponse>>> searchPosts(
            @Parameter(description = "검색어", example = "Spring")
            @RequestParam String keyword,
            
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<PostResponse> posts = postService.searchPosts(keyword, pageable);

        SuccessResponse<Page<PostResponse>> response = SuccessResponse.<Page<PostResponse>>builder()
                .data(posts)
                .meta(SuccessResponse.Meta.from(posts))
                .message("검색 결과: " + posts.getTotalElements() + "건")
                .timestamp(java.time.LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}



