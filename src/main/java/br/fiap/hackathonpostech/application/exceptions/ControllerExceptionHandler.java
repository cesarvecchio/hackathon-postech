package br.fiap.hackathonpostech.application.exceptions;

import br.fiap.hackathonpostech.application.validatons.ValidationError;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.NOT_FOUND;

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

    @ExceptionHandler(ClienteNaoExisteException.class)
    public ResponseEntity<StandardErrorException> clienteNaoExisteException(ClienteNaoExisteException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        StandardErrorException standardErrorException = StandardErrorException.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error("Cliente nao existe exception")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(status).body(standardErrorException);
    }

    @ExceptionHandler(LimiteQtdCartoesException.class)
    public ResponseEntity<StandardErrorException> limiteQtdCartoesException(LimiteQtdCartoesException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;

        StandardErrorException standardErrorException = StandardErrorException.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error("Limite qtd cartoes exception")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(status).body(standardErrorException);
    }

    @ExceptionHandler(CartaoExisteException.class)
    public ResponseEntity<StandardErrorException> cartaoExisteException(CartaoExisteException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        StandardErrorException standardErrorException = StandardErrorException.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error("Cartao existe exception")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(status).body(standardErrorException);
    }

    @ExceptionHandler(CartaoNaoExisteException.class)
    public ResponseEntity<StandardErrorException> cartaoNaoExisteException(CartaoNaoExisteException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        StandardErrorException standardErrorException = StandardErrorException.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error("Cartão não existe exception")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(status).body(standardErrorException);
    }

    @ExceptionHandler(CodigoCartaoInvalidoException.class)
    public ResponseEntity<StandardErrorException> codigoCartaoInvalidoException(CodigoCartaoInvalidoException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        StandardErrorException standardErrorException = StandardErrorException.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error("Código cartão inválido exception")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(status).body(standardErrorException);
    }

    @ExceptionHandler(ValidadeCartaoException.class)
    public ResponseEntity<StandardErrorException> validadeCartaoException(ValidadeCartaoException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        StandardErrorException standardErrorException = StandardErrorException.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error("Validade cartão exception")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(status).body(standardErrorException);
    }

    @ExceptionHandler(LimiteExcedidoCartaoException.class)
    public ResponseEntity<StandardErrorException> limiteExcedidoCartaoException(LimiteExcedidoCartaoException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.PAYMENT_REQUIRED;

        StandardErrorException standardErrorException = StandardErrorException.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error("Limite Execedido exception")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(status).body(standardErrorException);
    }

    @ExceptionHandler(PagamentoNaoEncontradoException.class)
    public ResponseEntity<StandardErrorException> pagamentoNaoEncontradoException(PagamentoNaoEncontradoException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        StandardErrorException standardErrorException = StandardErrorException.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error("Pagamento não encontrado exception")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(status).body(standardErrorException);
    }

    @ExceptionHandler(UsuarioNaoExisteException.class)
    public ResponseEntity<StandardErrorException> usuarioNaoExisteException(UsuarioNaoExisteException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.PAYMENT_REQUIRED;

        StandardErrorException standardErrorException = StandardErrorException.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error("Usuario nao existe exception")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(status).body(standardErrorException);
    }

    @ExceptionHandler(BadCredentialsException.class)
    ResponseEntity<StandardErrorException> handleWrongCredentials(BadCredentialsException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        StandardErrorException error = StandardErrorException.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error("Bad credentials exception")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(status.value()).body(error);
    }
}
