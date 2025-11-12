package com.sample.springtraining.exception;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.sample.springtraining.dto.common.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * 개선된 전역 예외 처리 핸들러
 * 
 * RFC 7807 (Problem Details for HTTP APIs) 표준 준수
 * 
 * 특징:
 * 1. 모든 에러에 traceId 부여 (분산 추적)
 * 2. RFC 7807 표준 준수
 * 3. 상세한 Validation 에러 정보
 * 4. 구조화된 로깅
 * 5. 환경별 에러 메시지 제어
 */
@Slf4j
@RestControllerAdvice
public class ImprovedGlobalExceptionHandler {

    private static final String ERROR_TYPE_BASE_URL = "https://api.example.com/errors";

    /**
     * ResourceNotFoundException 처리 (404)
     * 
     * 사용 예: throw new ResourceNotFoundException("게시물을 찾을 수 없습니다: " + id);
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            WebRequest request) {

        String traceId = generateTraceId(request);
        String instance = getRequestUri(request);

        log.warn("리소스를 찾을 수 없음 [traceId={}]: {}", traceId, ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .type(ERROR_TYPE_BASE_URL + "/resource-not-found")
                .title("Resource Not Found")
                .status(HttpStatus.NOT_FOUND.value())
                .detail(ex.getMessage())
                .instance(instance)
                .errorCode("ERR_RESOURCE_NOT_FOUND")
                .traceId(traceId)
                .timestamp(java.time.LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    /**
     * Validation 실패 처리 (400)
     * 
     * @Valid, @Validated 어노테이션으로 검증 실패 시 자동 호출
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        String traceId = generateTraceId(request);
        String instance = getRequestUri(request);

        // 필드별 에러 정보 수집
        List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::mapFieldError)
                .collect(Collectors.toList());

        log.warn("입력값 검증 실패 [traceId={}]: {} 개의 필드 에러", 
                traceId, fieldErrors.size());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .type(ERROR_TYPE_BASE_URL + "/validation-failed")
                .title("Validation Failed")
                .status(HttpStatus.BAD_REQUEST.value())
                .detail("입력값이 올바르지 않습니다. 세부 정보를 확인해주세요.")
                .instance(instance)
                .errorCode("ERR_VALIDATION")
                .traceId(traceId)
                .errors(fieldErrors)
                .timestamp(java.time.LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    /**
     * IllegalArgumentException 처리 (400)
     * 
     * 비즈니스 로직 검증 실패 시 사용
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex,
            WebRequest request) {

        String traceId = generateTraceId(request);
        String instance = getRequestUri(request);

        log.warn("잘못된 인자 [traceId={}]: {}", traceId, ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .type(ERROR_TYPE_BASE_URL + "/invalid-argument")
                .title("Invalid Argument")
                .status(HttpStatus.BAD_REQUEST.value())
                .detail(ex.getMessage())
                .instance(instance)
                .errorCode("ERR_INVALID_ARGUMENT")
                .traceId(traceId)
                .timestamp(java.time.LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    /**
     * IllegalStateException 처리 (409)
     * 
     * 상태 충돌 시 사용 (예: 이미 삭제된 리소스 수정 시도)
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(
            IllegalStateException ex,
            WebRequest request) {

        String traceId = generateTraceId(request);
        String instance = getRequestUri(request);

        log.warn("상태 충돌 [traceId={}]: {}", traceId, ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .type(ERROR_TYPE_BASE_URL + "/conflict")
                .title("Conflict")
                .status(HttpStatus.CONFLICT.value())
                .detail(ex.getMessage())
                .instance(instance)
                .errorCode("ERR_CONFLICT")
                .traceId(traceId)
                .timestamp(java.time.LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    /**
     * 일반 예외 처리 (500)
     * 
     * 예상하지 못한 서버 에러 처리
     * 프로덕션에서는 상세 에러 메시지를 숨김
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(
            Exception ex,
            WebRequest request) {

        String traceId = generateTraceId(request);
        String instance = getRequestUri(request);

        // 보안을 위해 상세 에러는 로그에만 기록
        log.error("예상치 못한 서버 에러 [traceId={}]", traceId, ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .type(ERROR_TYPE_BASE_URL + "/internal-error")
                .title("Internal Server Error")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .detail("서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해주세요.")
                .instance(instance)
                .errorCode("ERR_INTERNAL_SERVER_ERROR")
                .traceId(traceId)
                .timestamp(java.time.LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

    // === Private Helper Methods ===

    /**
     * Trace ID 생성
     * 실제 프로덕션에서는 분산 추적 시스템(Zipkin, Jaeger 등)의 Trace ID 사용
     */
    private String generateTraceId(WebRequest request) {
        // 헤더에서 기존 Trace ID 확인 (분산 추적)
        String traceId = request.getHeader("X-Trace-Id");
        if (traceId != null && !traceId.isEmpty()) {
            return traceId;
        }

        // 없으면 새로 생성
        return UUID.randomUUID().toString();
    }

    /**
     * 요청 URI 추출
     */
    private String getRequestUri(WebRequest request) {
        if (request instanceof ServletWebRequest) {
            HttpServletRequest httpRequest = ((ServletWebRequest) request).getRequest();
            return httpRequest.getRequestURI();
        }
        return request.getDescription(false).replace("uri=", "");
    }

    /**
     * Spring FieldError를 커스텀 FieldError로 변환
     */
    private ErrorResponse.FieldError mapFieldError(FieldError fieldError) {
        return ErrorResponse.FieldError.builder()
                .field(fieldError.getField())
                .rejectedValue(fieldError.getRejectedValue())
                .message(fieldError.getDefaultMessage())
                .code(fieldError.getCode())
                .build();
    }
}



