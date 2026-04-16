package dev.jramde.saas.exception;

import dev.jramde.saas.exception.response.JrErrorResponse;
import dev.jramde.saas.exception.response.JrErrorResponse.ValidationError;
import dev.jramde.saas.exception.response.JrUnauthorizedException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class JrGlobalExceptionHandler {

    /**
     * Catche toutes les exceptions métier.
     *
     * @param ex      : l'exception métier
     * @param request : l'url de la requête
     * @return une réponse avec le statut HTTP et le message d'erreur
     */
    @ExceptionHandler(JrBuisinessException.class)
    public ResponseEntity<JrErrorResponse> handle(final JrBuisinessException ex, final HttpServletRequest request) {
        final HttpStatus httpStatus = getHttpStatus(ex);
        final JrErrorResponse errorResponse = JrErrorResponse.builder()
                .message(ex.getMessage())
                .errorCode(httpStatus.name())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    /**
     * Catche les exceptions pour les ressources non trouvées.
     *
     * @param ex      : l'exception EntityNotFoundException
     * @param request : l'url de la requête
     * @return une réponse avec le statut HTTP et le message d'erreur
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<JrErrorResponse> handle(final EntityNotFoundException ex, final HttpServletRequest request) {
        final JrErrorResponse errorResponse = JrErrorResponse.builder()
                .message(ex.getMessage())
                .errorCode(HttpStatus.NOT_FOUND.name())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<JrErrorResponse> handle(
            final MethodArgumentNotValidException ex,
            final HttpServletRequest request) {

        final List<ValidationError> validationErrors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            final String fieldName = ((FieldError) error).getField();
            final String errorMessage = error.getDefaultMessage();
            final String errorCode = error.getDefaultMessage();

            validationErrors.add(ValidationError.builder()
                    .field(fieldName)
                    .message(errorMessage)
                    .errorCode(errorCode)
                    .build());
        });

        final JrErrorResponse errorResponse = JrErrorResponse.builder()
                .validationErrors(validationErrors)
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Retourne le code HTTP correspondant à l'exception métier.
     *
     * @param ex : l'exception métier
     * @return le code HTTP correspondant à l'exception métier
     */
    private HttpStatus getHttpStatus(final JrBuisinessException ex) {
        if (ex instanceof JrAlreadyExistException) {
            return HttpStatus.CONFLICT;
        } else if (ex instanceof JrUnauthorizedException) {
            return HttpStatus.UNAUTHORIZED;
        }

        return HttpStatus.BAD_REQUEST;
    }
}
