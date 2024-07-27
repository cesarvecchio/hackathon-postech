package br.fiap.hackathonpostech.application.exceptions;

import br.fiap.hackathonpostech.application.validatons.ValidationError;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ClienteExisteException.class)
    public ResponseEntity<StandardErrorException> clienteExisteException(ClienteExisteException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        StandardErrorException standardErrorException = StandardErrorException.builder()
            .timestamp(Instant.now())
            .status(status.value())
            .error("Cliente existe exception")
            .message(e.getMessage())
            .path(request.getRequestURI())
            .build();

        return ResponseEntity.status(status).body(standardErrorException);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidationError>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ValidationError> validationErrors = ex.getBindingResult().getAllErrors().stream()
            .map(error -> ValidationError.builder()
                .field(((FieldError) error).getField())
                .errorMessage(error.getDefaultMessage())
                .build()
            )
            .toList();
        return ResponseEntity.internalServerError().body(validationErrors);
    }
}
