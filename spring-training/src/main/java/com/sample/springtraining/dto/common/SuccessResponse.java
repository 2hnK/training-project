package com.sample.springtraining.dto.common;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * 성공 응답 표준 DTO
 * 
 * 대기업 표준 참고:
 * - Google API: 간결한 데이터 응답
 * - AWS: requestId를 통한 추적성
 * - Netflix: 불필요한 래퍼 최소화
 * 
 * 특징:
 * 1. null 필드 자동 제외 (@JsonInclude)
 * 2. 추적 가능한 traceId
 * 3. 선택적 메타데이터 (페이징 등)
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "API 성공 응답")
public class SuccessResponse<T> {

    @Schema(description = "응답 데이터", example = "{}")
    private final T data;

    @Schema(description = "응답 메시지", example = "요청이 성공적으로 처리되었습니다.")
    private final String message;

    @Schema(description = "요청 추적 ID", example = "550e8400-e29b-41d4-a716-446655440000")
    private final String traceId;

    @Schema(description = "응답 생성 시각", example = "2025-11-12T10:30:00")
    private final LocalDateTime timestamp;

    @Schema(description = "메타데이터 (페이징 정보 등)")
    private final Meta meta;

    /**
     * 기본 성공 응답 생성
     */
    public static <T> SuccessResponse<T> of(T data) {
        return SuccessResponse.<T>builder()
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * 메시지를 포함한 성공 응답 생성
     */
    public static <T> SuccessResponse<T> of(T data, String message) {
        return SuccessResponse.<T>builder()
                .data(data)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * TraceId를 포함한 성공 응답 생성 (추적이 필요한 중요 API)
     */
    public static <T> SuccessResponse<T> of(T data, String message, String traceId) {
        return SuccessResponse.<T>builder()
                .data(data)
                .message(message)
                .traceId(traceId)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * 페이징 메타데이터를 포함한 성공 응답 생성
     */
    public static <T> SuccessResponse<T> of(T data, Meta meta) {
        return SuccessResponse.<T>builder()
                .data(data)
                .meta(meta)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * 메타데이터 (페이징, 필터링 정보 등)
     */
    @Getter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Meta {
        
        @Schema(description = "현재 페이지 번호 (0-based)", example = "0")
        private final Integer page;
        
        @Schema(description = "페이지 크기", example = "20")
        private final Integer size;
        
        @Schema(description = "총 요소 개수", example = "150")
        private final Long totalElements;
        
        @Schema(description = "총 페이지 수", example = "8")
        private final Integer totalPages;
        
        @Schema(description = "첫 페이지 여부", example = "true")
        private final Boolean first;
        
        @Schema(description = "마지막 페이지 여부", example = "false")
        private final Boolean last;

        @Schema(description = "정렬 정보", example = "createdAt,desc")
        private final String sort;

        /**
         * Spring Data Page 객체로부터 Meta 생성
         */
        public static Meta from(org.springframework.data.domain.Page<?> page) {
            return Meta.builder()
                    .page(page.getNumber())
                    .size(page.getSize())
                    .totalElements(page.getTotalElements())
                    .totalPages(page.getTotalPages())
                    .first(page.isFirst())
                    .last(page.isLast())
                    .build();
        }
    }
}



