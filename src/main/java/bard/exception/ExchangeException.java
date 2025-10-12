// package bard.exception
package bard.exception;

import org.springframework.http.HttpStatus;

public class ExchangeException extends RuntimeException {
    private final String errorCode;
    private final HttpStatus httpStatus;

    public ExchangeException(String message, String errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

