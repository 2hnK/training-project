package com.sample.springtraining.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
public class ExampleAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger log = LoggerFactory.getLogger(ExampleAccessDeniedHandler.class);
    private final ObjectMapper objectMapper;

    public ExampleAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException ex) throws IOException {

        String requestId = Optional.ofNullable(request.getHeader("X-Request-Id"))
                                   .orElse(UUID.randomUUID().toString());

        log.warn("Access denied: method={} uri={} user={} requestId={}",
                request.getMethod(),
                request.getRequestURI(),
                request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "anonymous",
                requestId,
                ex);

        if (wantsJson(request)) {
            // API/아작스 요청 → RFC 7807 스타일의 ProblemDetail JSON 응답
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");

            ProblemDetail problem = ProblemDetail.forStatus(HttpServletResponse.SC_FORBIDDEN);
            problem.setTitle("Forbidden");
            problem.setDetail("이 리소스에 접근 권한이 없습니다.");
            problem.setProperty("requestId", requestId);

            objectMapper.writeValue(response.getWriter(), problem);
        } else {
            // 브라우저 HTML 요청 → 접근 거부 페이지로 리다이렉트
            response.sendRedirect(request.getContextPath() + "/access-denied");
        }
    }

    private boolean wantsJson(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        String xhr = request.getHeader("X-Requested-With");
        String uri = request.getRequestURI();
        String ctx = request.getContextPath() == null ? "" : request.getContextPath();

        return (accept != null && accept.contains("application/json"))
                || "XMLHttpRequest".equalsIgnoreCase(xhr)
                || uri.startsWith(ctx + "/api");
    }
}