package com.toystore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Ném khi không tìm thấy resource (404).
 * Ví dụ: Product với id 99 không tồn tại.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, Long id) {
        super(resourceName + " với id " + id + " không tồn tại");
    }

    public ResourceNotFoundException(String resourceName, String field, String value) {
        super(resourceName + " với " + field + " '" + value + "' không tồn tại");
    }
}
