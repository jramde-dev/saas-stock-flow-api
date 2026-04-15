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

    /**
     * Retourne le code HTTP correspondant à l'exception métier.
     *
     * @param ex : l'exception métier
     * @return le code HTTP correspondant à l'exception métier
     */
    private HttpStatus getHttpStatus(final JrBuisinessException ex) {
        if (ex instanceof JrAlreadyExistException) {
            return HttpStatus.CONFLICT;
        }

        return HttpStatus.BAD_REQUEST;
    }
}
