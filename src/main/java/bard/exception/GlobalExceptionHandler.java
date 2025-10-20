package bard.exception;

import bard.model.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExchangeException.class)
    public ResponseEntity<ApiResponse<?>> handleExchangeException(
            ExchangeException ex, HttpServletRequest request) {

        ApiResponse<?> response = ApiResponse.error(ex.getMessage(), ex.getErrorCode());

        return ResponseEntity.status(ex.getHttpStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        ApiResponse<?> response = ApiResponse.error(errorMessage, "VALIDATION_ERROR");

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGlobalException(
            Exception ex, HttpServletRequest request) {

        ApiResponse<?> response = ApiResponse.error(
                "Internal server error: " + ex.getMessage(),
                "INTERNAL_SERVER_ERROR"
        );

        return ResponseEntity.internalServerError().body(response);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidRequestException(
            InvalidRequestException ex, HttpServletRequest request) {

        ApiResponse<?> response = ApiResponse.error(ex.getMessage(), ex.getErrorCode());

        return ResponseEntity.badRequest().body(response);
    }
}
