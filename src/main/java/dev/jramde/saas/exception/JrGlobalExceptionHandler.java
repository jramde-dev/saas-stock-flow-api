package dev.jramde.saas.exception;

import dev.jramde.saas.exception.response.JrErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class JrGlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<JrErrorResponse> handle(final EntityNotFoundException ex, final HttpServletRequest request) {
        final JrErrorResponse errorResponse = JrErrorResponse.builder()
                .message(ex.getMessage())
                .errorCode(HttpStatus.NOT_FOUND.name())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
