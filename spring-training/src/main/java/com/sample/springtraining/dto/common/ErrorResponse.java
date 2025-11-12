package com.sample.springtraining.dto.common;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * 에러 응답 표준 DTO
 * 
 * RFC 7807 (Problem Details for HTTP APIs) 기반
 * https://www.rfc-editor.org/rfc/rfc7807
 * 
 * 대기업 표준 참고:
 * - Google: 구조화된 error 객체
 * - AWS: 명확한 에러 코드와 RequestId
 * - Stripe: 상세한 필드별 에러 정보
 * 
 * 특징:
 * 1. RFC 7807 준수 (type, title, status, detail)
 * 2. 추적을 위한 traceId/requestId
 * 3. Validation 에러 상세 정보
 * 4. 개발자 친화적 에러 메시지
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "API 에러 응답 (RFC 7807 기반)")
public class ErrorResponse {

    @Schema(
        description = "에러 타입 URI (문서화된 에러 타입 식별자)",
        example = "https://api.example.com/errors/validation-failed"
    )
    private final String type;

    @Schema(description = "에러 제목 (간단한 설명)", example = "Validation Failed")
    private final String title;

    @Schema(description = "HTTP 상태 코드", example = "400")
    private final int status;

    @Schema(description = "에러 상세 설명", example = "입력값이 유효하지 않습니다.")
    private final String detail;

    @Schema(description = "에러가 발생한 URI 경로", example = "/api/posts")
    private final String instance;

    @Schema(description = "에러 코드 (내부 분류용)", example = "ERR_VALIDATION_001")
    private final String errorCode;

    @Schema(description = "요청 추적 ID", example = "550e8400-e29b-41d4-a716-446655440000")
    private final String traceId;

    @Schema(description = "에러 발생 시각", example = "2025-11-12T10:30:00")
    private final LocalDateTime timestamp;

    @Schema(description = "필드별 에러 상세 정보 (Validation 실패 시)")
    private final List<FieldError> errors;

    @Schema(description = "추가 디버그 정보 (개발 환경에서만)")
    private final Map<String, Object> debugInfo;

    /**
     * 기본 에러 응답 생성
     */
    public static ErrorResponse of(int status, String title, String detail) {
        return ErrorResponse.builder()
                .status(status)
                .title(title)
                .detail(detail)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * 에러 코드를 포함한 에러 응답
     */
    public static ErrorResponse of(int status, String title, String detail, String errorCode) {
        return ErrorResponse.builder()
                .status(status)
                .title(title)
                .detail(detail)
                .errorCode(errorCode)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * 추적 ID를 포함한 에러 응답 (프로덕션 환경)
     */
    public static ErrorResponse of(int status, String title, String detail, String errorCode, String traceId) {
        return ErrorResponse.builder()
                .status(status)
                .title(title)
                .detail(detail)
                .errorCode(errorCode)
                .traceId(traceId)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * RFC 7807 완전 스펙 에러 응답
     */
    public static ErrorResponse of(
            String type,
            String title,
            int status,
            String detail,
            String instance,
            String errorCode,
            String traceId
    ) {
        return ErrorResponse.builder()
                .type(type)
                .title(title)
                .status(status)
                .detail(detail)
                .instance(instance)
                .errorCode(errorCode)
                .traceId(traceId)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Validation 에러 응답 (필드별 에러 포함)
     */
    public static ErrorResponse validation(String detail, List<FieldError> errors) {
        return ErrorResponse.builder()
                .status(400)
                .title("Validation Failed")
                .detail(detail)
                .errorCode("ERR_VALIDATION")
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * 필드별 에러 정보
     */
    @Getter
    @Builder
    @Schema(description = "필드 에러 상세 정보")
    public static class FieldError {
        
        @Schema(description = "에러가 발생한 필드명", example = "email")
        private final String field;
        
        @Schema(description = "거부된 입력값", example = "invalid-email")
        private final Object rejectedValue;
        
        @Schema(description = "에러 메시지", example = "이메일 형식이 올바르지 않습니다.")
        private final String message;
        
        @Schema(description = "에러 코드", example = "email.invalid")
        private final String code;
    }
}



