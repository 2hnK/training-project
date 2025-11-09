package com.sample.springtraining.exception;

import lombok.Getter;

/**
 * 리소스를 찾을 수 없을 때 발생하는 예외
 * RESTful API 표준에 따라 404 Not Found 응답에 사용
 */
@Getter
public class ResourceNotFoundException extends RuntimeException {
    
    private String resourceName;
    private String fieldName;
    private Object fieldValue;
    
    /**
     * @param resourceName 리소스 이름 (예: "Post", "User")
     * @param fieldName 필드 이름 (예: "id", "email")
     * @param fieldValue 필드 값
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
}

