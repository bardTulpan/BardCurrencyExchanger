package bard.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExchangeException.class)
    public void handleExchangeException(
            ExchangeException ex, HttpServletResponse response) throws IOException {
        response.sendError(ex.getHttpStatus().value());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleValidationException(MethodArgumentNotValidException ex) {
        // Просто возвращаем 400 статус, без тела ответа
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGlobalException(Exception ex) {
        return ex.getMessage();
        // Просто возвращаем 500 статус, без тела ответа
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleDataAccessException(Exception ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleInvalidRequestException(InvalidRequestException ex) {
        // Просто возвращаем 400 статус, без тела ответа
    }

    @ExceptionHandler(MissingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleCurrencyCodeMissing(MissingException ex) {
        // Просто возвращаем 400 статус, без тела ответа
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // Изменил на 404, так как логичнее для "Not Found"
    public void handleNotFoundException(NotFoundException exception) {
        // Просто возвращаем 404 статус, без тела ответа
    }
}


//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(ExchangeException.class)
//    public ResponseEntity<ApiResponse<?>> handleExchangeException(
//            ExchangeException ex, HttpServletRequest request) {
//
//        ApiResponse<?> response = ApiResponse.error(ex.getMessage(), ex.getErrorCode());
//
//        return ResponseEntity.status(ex.getHttpStatus()).body(response);
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ApiResponse<?>> handleValidationException(
//            MethodArgumentNotValidException ex, HttpServletRequest request) {
//
//        String errorMessage = ex.getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .map(FieldError::getDefaultMessage)
//                .collect(Collectors.joining(", "));
//
//        ApiResponse<?> response = ApiResponse.error(errorMessage, "VALIDATION_ERROR");
//
//        return ResponseEntity.badRequest().body(response);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiResponse<?>> handleGlobalException(
//            Exception ex, HttpServletRequest request) {
//
//        ApiResponse<?> response = ApiResponse.error(
//                "Internal server error: " + ex.getMessage(),
//                "INTERNAL_SERVER_ERROR"
//        );
//
//        return ResponseEntity.internalServerError().body(response);
//    }
//
//    @ExceptionHandler(InvalidRequestException.class)
//    public ResponseEntity<ApiResponse<?>> handleInvalidRequestException(
//            InvalidRequestException ex, HttpServletRequest request) {
//
//        ApiResponse<?> response = ApiResponse.error(ex.getMessage(), ex.getErrorCode());
//
//        return ResponseEntity.badRequest().body(response);
//    }
//
//    @ExceptionHandler(MissingException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String handleCurrencyCodeMissing(MissingException ex) {
//        return ex.getMessage();
//    }
//
//    @ExceptionHandler(NotFoundException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String handleNotFoundException(NotFoundException exception) {
//        return exception.getMessage();
//    }
//}
