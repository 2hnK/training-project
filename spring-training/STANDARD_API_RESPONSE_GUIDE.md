# 표준 API 응답 가이드

## 📚 개요

이 문서는 대기업 수준의 현대적이고 표준적인 API 응답 구조를 설명합니다.

### 참고 표준

- **RFC 7807**: Problem Details for HTTP APIs
- **Spring Framework 6+**: ProblemDetail 공식 지원
- **대기업 Best Practices**: Google, AWS, Microsoft, Netflix, Stripe

---

## 🎯 핵심 원칙

### 1. **분리된 성공/에러 응답 구조**
- 성공: `SuccessResponse<T>` - 간결하고 필요한 정보만
- 에러: `ErrorResponse` - RFC 7807 표준 준수

### 2. **추적성 (Traceability)**
- 모든 중요 API 응답에 `traceId` 포함
- 분산 시스템 디버깅 및 로그 추적 용이

### 3. **명확한 HTTP 상태 코드**
- 200: 조회 성공
- 201: 생성 성공
- 204: 삭제 성공 (본문 없음)
- 400: 클라이언트 에러
- 404: 리소스 없음
- 500: 서버 에러

### 4. **페이징 표준화**
- Meta 객체로 페이징 정보 제공
- `page`, `size`, `totalElements`, `totalPages` 포함

---

## ✅ 성공 응답 (SuccessResponse)

### 기본 구조

```json
{
  "data": { ... },
  "message": "요청이 성공적으로 처리되었습니다.",
  "traceId": "550e8400-e29b-41d4-a716-446655440000",
  "timestamp": "2025-11-12T10:30:00",
  "meta": { ... }
}
```

### 필드 설명

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `data` | T | ✅ | 실제 응답 데이터 |
| `message` | String | ❌ | 사용자 친화적 메시지 |
| `traceId` | String | ❌ | 요청 추적 ID (중요 API에서 사용) |
| `timestamp` | LocalDateTime | ✅ | 응답 생성 시각 |
| `meta` | Meta | ❌ | 메타데이터 (페이징 등) |

### 사용 예제

#### 1. 단순 조회 (가장 간결한 형태)

```java
@GetMapping("/{id}")
public ResponseEntity<SuccessResponse<PostResponse>> getPost(@PathVariable Long id) {
    PostResponse post = postService.findById(id);
    return ResponseEntity.ok(SuccessResponse.of(post));
}
```

**응답:**
```json
{
  "data": {
    "id": 1,
    "title": "Spring Boot 가이드",
    "content": "...",
    "author": "홍길동"
  },
  "timestamp": "2025-11-12T10:30:00"
}
```

#### 2. 메시지 포함 (생성/수정 시)

```java
@PostMapping
public ResponseEntity<SuccessResponse<PostResponse>> createPost(@Valid @RequestBody PostCreateRequest request) {
    PostResponse response = postService.createPost(request);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(SuccessResponse.of(response, "게시물이 생성되었습니다."));
}
```

**응답:**
```json
{
  "data": {
    "id": 123,
    "title": "새 게시물"
  },
  "message": "게시물이 생성되었습니다.",
  "timestamp": "2025-11-12T10:30:00"
}
```

#### 3. 페이징 메타데이터 포함

```java
@GetMapping
public ResponseEntity<SuccessResponse<Page<PostResponse>>> getPosts(Pageable pageable) {
    Page<PostResponse> posts = postService.findAll(pageable);
    
    SuccessResponse<Page<PostResponse>> response = SuccessResponse.<Page<PostResponse>>builder()
            .data(posts)
            .meta(SuccessResponse.Meta.from(posts))
            .timestamp(LocalDateTime.now())
            .build();
    
    return ResponseEntity.ok(response);
}
```

**응답:**
```json
{
  "data": {
    "content": [
      { "id": 1, "title": "게시물 1" },
      { "id": 2, "title": "게시물 2" }
    ],
    "pageable": { ... },
    "totalElements": 150
  },
  "meta": {
    "page": 0,
    "size": 20,
    "totalElements": 150,
    "totalPages": 8,
    "first": true,
    "last": false
  },
  "timestamp": "2025-11-12T10:30:00"
}
```

---

## ❌ 에러 응답 (ErrorResponse)

### RFC 7807 기반 구조

```json
{
  "type": "https://api.example.com/errors/validation-failed",
  "title": "Validation Failed",
  "status": 400,
  "detail": "입력값이 올바르지 않습니다.",
  "instance": "/api/v1/posts",
  "errorCode": "ERR_VALIDATION",
  "traceId": "550e8400-e29b-41d4-a716-446655440000",
  "timestamp": "2025-11-12T10:30:00",
  "errors": [ ... ]
}
```

### 필드 설명 (RFC 7807)

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `type` | URI | ❌ | 에러 타입 식별자 (문서화된 에러 페이지) |
| `title` | String | ✅ | 에러 제목 (간단한 설명) |
| `status` | int | ✅ | HTTP 상태 코드 |
| `detail` | String | ✅ | 상세 에러 메시지 |
| `instance` | URI | ❌ | 에러가 발생한 요청 경로 |
| `errorCode` | String | ✅ | 내부 에러 코드 (분류용) |
| `traceId` | String | ✅ | 요청 추적 ID |
| `timestamp` | LocalDateTime | ✅ | 에러 발생 시각 |
| `errors` | List | ❌ | 필드별 에러 상세 (Validation) |

### 에러 응답 예제

#### 1. 리소스 없음 (404)

```json
{
  "type": "https://api.example.com/errors/resource-not-found",
  "title": "Resource Not Found",
  "status": 404,
  "detail": "ID가 999인 게시물을 찾을 수 없습니다.",
  "instance": "/api/v1/posts/999",
  "errorCode": "ERR_RESOURCE_NOT_FOUND",
  "traceId": "550e8400-e29b-41d4-a716-446655440000",
  "timestamp": "2025-11-12T10:30:00"
}
```

#### 2. Validation 실패 (400)

```json
{
  "type": "https://api.example.com/errors/validation-failed",
  "title": "Validation Failed",
  "status": 400,
  "detail": "입력값이 올바르지 않습니다. 세부 정보를 확인해주세요.",
  "instance": "/api/v1/posts",
  "errorCode": "ERR_VALIDATION",
  "traceId": "550e8400-e29b-41d4-a716-446655440000",
  "timestamp": "2025-11-12T10:30:00",
  "errors": [
    {
      "field": "title",
      "rejectedValue": "",
      "message": "제목은 필수입니다.",
      "code": "NotBlank"
    },
    {
      "field": "email",
      "rejectedValue": "invalid-email",
      "message": "이메일 형식이 올바르지 않습니다.",
      "code": "Email"
    }
  ]
}
```

#### 3. 비즈니스 로직 에러 (400)

```java
if (post.isDeleted()) {
    throw new IllegalStateException("삭제된 게시물은 수정할 수 없습니다.");
}
```

**응답:**
```json
{
  "type": "https://api.example.com/errors/conflict",
  "title": "Conflict",
  "status": 409,
  "detail": "삭제된 게시물은 수정할 수 없습니다.",
  "instance": "/api/v1/posts/123",
  "errorCode": "ERR_CONFLICT",
  "traceId": "550e8400-e29b-41d4-a716-446655440000",
  "timestamp": "2025-11-12T10:30:00"
}
```

#### 4. 서버 에러 (500)

```json
{
  "type": "https://api.example.com/errors/internal-error",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해주세요.",
  "instance": "/api/v1/posts",
  "errorCode": "ERR_INTERNAL_SERVER_ERROR",
  "traceId": "550e8400-e29b-41d4-a716-446655440000",
  "timestamp": "2025-11-12T10:30:00"
}
```

---

## 🏢 대기업 Best Practices 비교

### Google API

**특징:**
- 간결한 데이터 응답
- 에러는 `error` 객체로 래핑
- Pagination: `nextPageToken`

```json
{
  "items": [...],
  "nextPageToken": "..."
}
```

### AWS API

**특징:**
- RequestId 기반 추적
- 명확한 에러 코드 체계

```json
{
  "Error": {
    "Code": "ResourceNotFoundException",
    "Message": "..."
  },
  "RequestId": "..."
}
```

### Microsoft Graph API

**특징:**
- OData 표준
- @odata 접두사 메타데이터

```json
{
  "value": [...],
  "@odata.nextLink": "...",
  "@odata.count": 100
}
```

### Stripe API

**특징:**
- 상세한 필드별 에러
- 명확한 에러 타입 분류

```json
{
  "error": {
    "type": "card_error",
    "code": "card_declined",
    "message": "..."
  }
}
```

---

## 🔧 구현 가이드

### 1. 컨트롤러에서 사용

```java
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // ✅ 간단한 조회
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<PostResponse>> getPost(@PathVariable Long id) {
        return ResponseEntity.ok(SuccessResponse.of(postService.findById(id)));
    }

    // ✅ 생성 (메시지 포함)
    @PostMapping
    public ResponseEntity<SuccessResponse<PostResponse>> createPost(@Valid @RequestBody PostCreateRequest request) {
        PostResponse response = postService.createPost(request);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(SuccessResponse.of(response, "게시물이 생성되었습니다."));
    }

    // ✅ 삭제 (본문 없음)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
```

### 2. 예외 처리

```java
// Service Layer
public PostResponse findById(Long id) {
    return postRepository.findById(id)
        .map(this::toResponse)
        .orElseThrow(() -> new ResourceNotFoundException("게시물을 찾을 수 없습니다: " + id));
}

// GlobalExceptionHandler가 자동으로 ErrorResponse 변환
```

### 3. Validation

```java
// DTO
public class PostCreateRequest {
    
    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 100, message = "제목은 100자를 초과할 수 없습니다.")
    private String title;
    
    @NotBlank(message = "내용은 필수입니다.")
    private String content;
    
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String authorEmail;
}

// Controller
@PostMapping
public ResponseEntity<SuccessResponse<PostResponse>> createPost(
        @Valid @RequestBody PostCreateRequest request) {
    // Validation 실패 시 자동으로 ErrorResponse 반환
}
```

---

## 📊 적용 전/후 비교

### ❌ 기존 방식 (개선 전)

```java
// 일관성 없는 응답 구조
@GetMapping("/{id}")
public ResponseEntity<ApiResponse<PostResponse>> getPost(@PathVariable Long id) {
    return ResponseEntity.ok(ApiResponse.success(postService.findById(id)));
}

// 응답
{
  "success": true,
  "message": "Success",
  "data": { ... },
  "timestamp": "...",
  "errorCode": null  // null 필드 불필요하게 노출
}
```

**문제점:**
1. `success` 필드 불필요 (HTTP 상태 코드로 충분)
2. null 필드 노출
3. 추적 ID 없음
4. 페이징 정보 부족
5. RFC 7807 미준수

### ✅ 개선 후

```java
@GetMapping("/{id}")
public ResponseEntity<SuccessResponse<PostResponse>> getPost(@PathVariable Long id) {
    return ResponseEntity.ok(SuccessResponse.of(postService.findById(id)));
}

// 응답 (간결함)
{
  "data": { ... },
  "timestamp": "..."
}
```

**개선점:**
1. 필요한 정보만 포함
2. RFC 7807 표준 준수
3. TraceId로 추적성 확보
4. 페이징 메타데이터 표준화
5. 대기업 Best Practice 반영

---

## 🎓 권장 사항

### 언제 무엇을 사용할까?

#### SuccessResponse 사용

- ✅ 단순 조회: `SuccessResponse.of(data)`
- ✅ 생성/수정: `SuccessResponse.of(data, message)`
- ✅ 페이징 목록: `SuccessResponse.of(data, meta)`
- ✅ 중요 API: `SuccessResponse.of(data, message, traceId)`

#### ErrorResponse 사용

- ✅ 자동 변환: `GlobalExceptionHandler`가 처리
- ✅ 수동 생성 필요 시: `ErrorResponse.of(...)`

### TraceId 사용 시기

- ✅ 결제/주문 등 중요 트랜잭션
- ✅ 외부 API 연동
- ✅ 프로덕션 환경 디버깅
- ❌ 단순 조회 API (선택적)

### 페이징 구조

```java
// ✅ 권장: Spring Data Page + Meta
Page<PostResponse> posts = postService.findAll(pageable);
SuccessResponse.of(posts, SuccessResponse.Meta.from(posts));

// ❌ 비권장: 커스텀 페이징 구조 (표준화 깨짐)
```

---

## 📚 추가 자료

- [RFC 7807 - Problem Details for HTTP APIs](https://www.rfc-editor.org/rfc/rfc7807)
- [Spring Boot 3.x Error Handling](https://spring.io/guides/tutorials/rest/)
- [Google API Design Guide](https://cloud.google.com/apis/design)
- [Microsoft REST API Guidelines](https://github.com/microsoft/api-guidelines)

---

## ✨ 결론

현대적인 API 응답 구조는:

1. **간결함** - 불필요한 필드 최소화
2. **표준 준수** - RFC 7807, HTTP 상태 코드
3. **추적성** - TraceId 활용
4. **일관성** - 모든 API 동일한 구조
5. **확장성** - 메타데이터로 추가 정보 제공

이 가이드를 따르면 대기업 수준의 API를 구현할 수 있습니다! 🚀



