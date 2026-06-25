package com.toystore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Ném khi tạo resource bị trùng lặp (409).
 * Ví dụ: Email đã tồn tại, review đã tồn tại cho sản phẩm này.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String resourceName, String field, String value) {
        super(resourceName + " với " + field + " '" + value + "' đã tồn tại");
    }
}
