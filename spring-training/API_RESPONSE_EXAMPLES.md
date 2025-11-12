# API 응답 실전 예제 모음

## 🎯 목차

1. [성공 응답 패턴](#성공-응답-패턴)
2. [에러 응답 패턴](#에러-응답-패턴)
3. [특수 상황 처리](#특수-상황-처리)
4. [대기업 실제 사례](#대기업-실제-사례)

---

## 성공 응답 패턴

### 1. 단건 조회 (GET)

#### Request
```http
GET /api/v1/posts/123
Accept: application/json
```

#### Response
```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "data": {
    "id": 123,
    "title": "Spring Boot 완벽 가이드",
    "content": "Spring Boot는...",
    "author": "홍길동",
    "authorId": 1,
    "createdAt": "2025-11-10T10:00:00",
    "updatedAt": "2025-11-12T09:30:00"
  },
  "timestamp": "2025-11-12T10:30:00"
}
```

---

### 2. 목록 조회 with 페이징 (GET)

#### Request
```http
GET /api/v1/posts?page=0&size=20&sortBy=createdAt&direction=DESC
Accept: application/json
```

#### Response
```http
HTTP/1.1 200 OK
Content-Type: application/json
Link: </api/v1/posts?page=1&size=20>; rel="next"

{
  "data": {
    "content": [
      {
        "id": 150,
        "title": "최신 게시물",
        "author": "홍길동",
        "createdAt": "2025-11-12T10:00:00"
      },
      {
        "id": 149,
        "title": "두 번째 게시물",
        "author": "김철수",
        "createdAt": "2025-11-12T09:00:00"
      }
      // ... 18개 더
    ]
  },
  "meta": {
    "page": 0,
    "size": 20,
    "totalElements": 150,
    "totalPages": 8,
    "first": true,
    "last": false,
    "sort": "createdAt,desc"
  },
  "timestamp": "2025-11-12T10:30:00"
}
```

---

### 3. 생성 (POST)

#### Request
```http
POST /api/v1/posts
Content-Type: application/json
X-Trace-Id: client-generated-uuid

{
  "title": "새 게시물",
  "content": "내용입니다."
}
```

#### Response
```http
HTTP/1.1 201 Created
Content-Type: application/json
Location: /api/v1/posts/151

{
  "data": {
    "id": 151,
    "title": "새 게시물",
    "content": "내용입니다.",
    "author": "홍길동",
    "createdAt": "2025-11-12T10:30:00"
  },
  "message": "게시물이 성공적으로 생성되었습니다.",
  "timestamp": "2025-11-12T10:30:00"
}
```

---

### 4. 수정 (PUT)

#### Request
```http
PUT /api/v1/posts/151
Content-Type: application/json

{
  "title": "수정된 게시물",
  "content": "수정된 내용입니다."
}
```

#### Response
```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "data": {
    "id": 151,
    "title": "수정된 게시물",
    "content": "수정된 내용입니다.",
    "author": "홍길동",
    "updatedAt": "2025-11-12T11:00:00"
  },
  "message": "게시물이 수정되었습니다.",
  "timestamp": "2025-11-12T11:00:00"
}
```

---

### 5. 삭제 (DELETE)

#### Request
```http
DELETE /api/v1/posts/151
```

#### Response
```http
HTTP/1.1 204 No Content
```

**주의:** 204 응답은 본문(body)이 없습니다.

---

### 6. 검색 (GET with Query)

#### Request
```http
GET /api/v1/posts/search?keyword=Spring&page=0&size=20
Accept: application/json
```

#### Response
```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "data": {
    "content": [
      {
        "id": 123,
        "title": "Spring Boot 완벽 가이드",
        "content": "Spring Boot는...",
        "author": "홍길동"
      },
      {
        "id": 87,
        "title": "Spring Security 입문",
        "content": "보안은...",
        "author": "김철수"
      }
    ]
  },
  "meta": {
    "page": 0,
    "size": 20,
    "totalElements": 42,
    "totalPages": 3
  },
  "message": "검색 결과: 42건",
  "timestamp": "2025-11-12T10:30:00"
}
```

---

## 에러 응답 패턴

### 1. 리소스 없음 (404)

#### Request
```http
GET /api/v1/posts/999999
Accept: application/json
```

#### Response
```http
HTTP/1.1 404 Not Found
Content-Type: application/problem+json

{
  "type": "https://api.example.com/errors/resource-not-found",
  "title": "Resource Not Found",
  "status": 404,
  "detail": "ID가 999999인 게시물을 찾을 수 없습니다.",
  "instance": "/api/v1/posts/999999",
  "errorCode": "ERR_RESOURCE_NOT_FOUND",
  "traceId": "550e8400-e29b-41d4-a716-446655440000",
  "timestamp": "2025-11-12T10:30:00"
}
```

---

### 2. Validation 실패 (400)

#### Request
```http
POST /api/v1/posts
Content-Type: application/json

{
  "title": "",
  "content": "내용입니다.",
  "authorEmail": "invalid-email"
}
```

#### Response
```http
HTTP/1.1 400 Bad Request
Content-Type: application/problem+json

{
  "type": "https://api.example.com/errors/validation-failed",
  "title": "Validation Failed",
  "status": 400,
  "detail": "입력값이 올바르지 않습니다. 세부 정보를 확인해주세요.",
  "instance": "/api/v1/posts",
  "errorCode": "ERR_VALIDATION",
  "traceId": "550e8400-e29b-41d4-a716-446655440001",
  "timestamp": "2025-11-12T10:30:00",
  "errors": [
    {
      "field": "title",
      "rejectedValue": "",
      "message": "제목은 필수입니다.",
      "code": "NotBlank"
    },
    {
      "field": "authorEmail",
      "rejectedValue": "invalid-email",
      "message": "이메일 형식이 올바르지 않습니다.",
      "code": "Email"
    }
  ]
}
```

---

### 3. 비즈니스 로직 에러 (409 Conflict)

#### Request
```http
PUT /api/v1/posts/123
Content-Type: application/json

{
  "title": "수정",
  "content": "내용"
}
```

#### Response (게시물이 이미 삭제된 경우)
```http
HTTP/1.1 409 Conflict
Content-Type: application/problem+json

{
  "type": "https://api.example.com/errors/conflict",
  "title": "Conflict",
  "status": 409,
  "detail": "삭제된 게시물은 수정할 수 없습니다.",
  "instance": "/api/v1/posts/123",
  "errorCode": "ERR_CONFLICT",
  "traceId": "550e8400-e29b-41d4-a716-446655440002",
  "timestamp": "2025-11-12T10:30:00"
}
```

---

### 4. 권한 없음 (403 Forbidden)

#### Request
```http
DELETE /api/v1/posts/123
Authorization: Bearer user-token
```

#### Response (다른 사용자의 게시물 삭제 시도)
```http
HTTP/1.1 403 Forbidden
Content-Type: application/problem+json

{
  "type": "https://api.example.com/errors/access-denied",
  "title": "Access Denied",
  "status": 403,
  "detail": "게시물 작성자만 삭제할 수 있습니다.",
  "instance": "/api/v1/posts/123",
  "errorCode": "ERR_ACCESS_DENIED",
  "traceId": "550e8400-e29b-41d4-a716-446655440003",
  "timestamp": "2025-11-12T10:30:00"
}
```

---

### 5. 인증 필요 (401 Unauthorized)

#### Request
```http
POST /api/v1/posts
Content-Type: application/json

{
  "title": "새 게시물",
  "content": "내용"
}
```

#### Response (인증 토큰 없음)
```http
HTTP/1.1 401 Unauthorized
WWW-Authenticate: Bearer realm="API"
Content-Type: application/problem+json

{
  "type": "https://api.example.com/errors/unauthorized",
  "title": "Unauthorized",
  "status": 401,
  "detail": "인증이 필요합니다. 로그인 후 다시 시도해주세요.",
  "instance": "/api/v1/posts",
  "errorCode": "ERR_UNAUTHORIZED",
  "traceId": "550e8400-e29b-41d4-a716-446655440004",
  "timestamp": "2025-11-12T10:30:00"
}
```

---

### 6. 서버 에러 (500)

#### Request
```http
GET /api/v1/posts/123
Accept: application/json
```

#### Response (데이터베이스 연결 실패 등)
```http
HTTP/1.1 500 Internal Server Error
Content-Type: application/problem+json

{
  "type": "https://api.example.com/errors/internal-error",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해주세요.",
  "instance": "/api/v1/posts/123",
  "errorCode": "ERR_INTERNAL_SERVER_ERROR",
  "traceId": "550e8400-e29b-41d4-a716-446655440005",
  "timestamp": "2025-11-12T10:30:00"
}
```

**주의:** 프로덕션 환경에서는 상세한 에러 메시지를 숨기고, `traceId`로만 추적합니다.

---

### 7. Rate Limit 초과 (429)

#### Request
```http
POST /api/v1/posts
Content-Type: application/json
```

#### Response (요청 횟수 제한 초과)
```http
HTTP/1.1 429 Too Many Requests
Retry-After: 3600
Content-Type: application/problem+json

{
  "type": "https://api.example.com/errors/rate-limit-exceeded",
  "title": "Rate Limit Exceeded",
  "status": 429,
  "detail": "API 요청 제한을 초과했습니다. 1시간 후 다시 시도해주세요.",
  "instance": "/api/v1/posts",
  "errorCode": "ERR_RATE_LIMIT_EXCEEDED",
  "traceId": "550e8400-e29b-41d4-a716-446655440006",
  "timestamp": "2025-11-12T10:30:00"
}
```

---

## 특수 상황 처리

### 1. 빈 목록 조회

#### Response
```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "data": {
    "content": []
  },
  "meta": {
    "page": 0,
    "size": 20,
    "totalElements": 0,
    "totalPages": 0,
    "first": true,
    "last": true
  },
  "timestamp": "2025-11-12T10:30:00"
}
```

**주의:** 빈 결과는 200 OK입니다. 404가 아닙니다!

---

### 2. 부분 수정 (PATCH)

#### Request
```http
PATCH /api/v1/posts/123
Content-Type: application/json

{
  "title": "제목만 수정"
}
```

#### Response
```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "data": {
    "id": 123,
    "title": "제목만 수정",
    "content": "기존 내용 유지",
    "updatedAt": "2025-11-12T11:00:00"
  },
  "message": "게시물이 수정되었습니다.",
  "timestamp": "2025-11-12T11:00:00"
}
```

---

### 3. Bulk 작업

#### Request
```http
DELETE /api/v1/posts/bulk
Content-Type: application/json

{
  "ids": [123, 124, 125]
}
```

#### Response
```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "data": {
    "deleted": [123, 124],
    "failed": [
      {
        "id": 125,
        "reason": "권한 없음"
      }
    ]
  },
  "message": "2개의 게시물이 삭제되었습니다. 1개 실패.",
  "timestamp": "2025-11-12T11:00:00"
}
```

---

### 4. 비동기 작업 (202 Accepted)

#### Request
```http
POST /api/v1/posts/123/export
Accept: application/json
```

#### Response
```http
HTTP/1.1 202 Accepted
Content-Type: application/json
Location: /api/v1/jobs/export-456

{
  "data": {
    "jobId": "export-456",
    "status": "PENDING",
    "estimatedTime": 300
  },
  "message": "내보내기 작업이 시작되었습니다.",
  "timestamp": "2025-11-12T11:00:00"
}
```

---

## 대기업 실제 사례

### Google Cloud API

```json
{
  "items": [...],
  "nextPageToken": "CiAKGjBpNDd2Nmp...",
  "kind": "storage#objects"
}
```

**특징:**
- 간결한 구조
- `nextPageToken`으로 페이징
- `kind` 필드로 리소스 타입 식별

---

### AWS API

```json
{
  "Items": [...],
  "Count": 20,
  "ScannedCount": 20,
  "LastEvaluatedKey": {...}
}
```

**에러:**
```json
{
  "__type": "ResourceNotFoundException",
  "message": "Requested resource not found"
}
```

**특징:**
- 명확한 에러 타입
- 메타데이터 최소화
- X-Amzn-RequestId 헤더로 추적

---

### Microsoft Graph API

```json
{
  "value": [...],
  "@odata.context": "https://graph.microsoft.com/v1.0/$metadata#users",
  "@odata.nextLink": "https://graph.microsoft.com/v1.0/users?$skiptoken=..."
}
```

**특징:**
- OData 표준
- `@odata` 접두사
- 절대 URL 링크

---

### Stripe API

```json
{
  "object": "list",
  "data": [...],
  "has_more": true,
  "url": "/v1/charges"
}
```

**에러:**
```json
{
  "error": {
    "type": "card_error",
    "code": "card_declined",
    "message": "Your card was declined.",
    "param": "card_number"
  }
}
```

**특징:**
- 상세한 에러 타입 분류
- `param` 필드로 에러 위치 명시
- 개발자 친화적 메시지

---

### Netflix API (내부)

```json
{
  "title": "...",
  "year": 2024,
  "rating": 8.5
}
```

**특징:**
- 래퍼 없이 데이터 직접 반환
- 극도로 간결함
- GraphQL 사용 (REST 보완)

---

## 프론트엔드 연동 예제

### React/TypeScript

```typescript
// types/api.ts
export interface SuccessResponse<T> {
  data: T;
  message?: string;
  traceId?: string;
  timestamp: string;
  meta?: PageMeta;
}

export interface PageMeta {
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
}

export interface ErrorResponse {
  type?: string;
  title: string;
  status: number;
  detail: string;
  instance?: string;
  errorCode: string;
  traceId?: string;
  timestamp: string;
  errors?: FieldError[];
}

export interface FieldError {
  field: string;
  rejectedValue: any;
  message: string;
  code: string;
}

// api/posts.ts
export class PostApi {
  async getPost(id: number): Promise<Post> {
    const response = await fetch(`/api/v1/posts/${id}`);
    
    if (!response.ok) {
      const error: ErrorResponse = await response.json();
      throw new ApiError(error);
    }
    
    const result: SuccessResponse<Post> = await response.json();
    return result.data;
  }
  
  async getPosts(page = 0, size = 20): Promise<{ posts: Post[], meta: PageMeta }> {
    const response = await fetch(`/api/v1/posts?page=${page}&size=${size}`);
    
    if (!response.ok) {
      const error: ErrorResponse = await response.json();
      throw new ApiError(error);
    }
    
    const result: SuccessResponse<{ content: Post[] }> = await response.json();
    return {
      posts: result.data.content,
      meta: result.meta!
    };
  }
}

// Error handling
export class ApiError extends Error {
  constructor(public readonly response: ErrorResponse) {
    super(response.detail);
    this.name = 'ApiError';
  }
  
  get traceId(): string | undefined {
    return this.response.traceId;
  }
  
  get fieldErrors(): FieldError[] {
    return this.response.errors || [];
  }
}

// 사용 예제
try {
  const post = await postApi.getPost(123);
  console.log(post.title);
} catch (err) {
  if (err instanceof ApiError) {
    console.error(`[${err.traceId}] ${err.message}`);
    
    // Validation 에러 처리
    err.fieldErrors.forEach(fe => {
      showFieldError(fe.field, fe.message);
    });
  }
}
```

---

## 테스트 예제

### Spring Boot Test

```java
@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void 게시물_조회_성공() throws Exception {
        mockMvc.perform(get("/api/v1/posts/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.success").doesNotExist()); // ✅ success 필드 없음
    }
    
    @Test
    void 게시물_조회_실패_404() throws Exception {
        mockMvc.perform(get("/api/v1/posts/999999")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").exists())
                .andExpect(jsonPath("$.title").value("Resource Not Found"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").exists())
                .andExpect(jsonPath("$.errorCode").value("ERR_RESOURCE_NOT_FOUND"))
                .andExpect(jsonPath("$.traceId").exists());
    }
    
    @Test
    void 게시물_생성_Validation_실패() throws Exception {
        mockMvc.perform(post("/api/v1/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"\",\"content\":\"내용\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("ERR_VALIDATION"))
                .andExpect(jsonPath("$.errors[0].field").value("title"))
                .andExpect(jsonPath("$.errors[0].message").exists());
    }
}
```

---

## ✅ 체크리스트

API 응답 구현 시 확인:

- [ ] HTTP 상태 코드가 의미와 일치하는가?
- [ ] 성공 응답에 불필요한 필드가 없는가?
- [ ] 에러 응답이 RFC 7807을 준수하는가?
- [ ] TraceId가 포함되어 있는가? (중요 API)
- [ ] 페이징 메타데이터가 표준화되어 있는가?
- [ ] Validation 에러에 필드별 상세 정보가 있는가?
- [ ] 프론트엔드가 파싱하기 쉬운 구조인가?
- [ ] null 필드가 자동으로 제외되는가?
- [ ] 문서화(Swagger)가 되어 있는가?
- [ ] 테스트 코드가 작성되어 있는가?

---

**이 예제들을 참고하여 일관되고 표준적인 API를 구현하세요!** 🚀



