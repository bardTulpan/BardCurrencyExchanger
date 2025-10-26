package bard.exception;

public class MissingException extends RuntimeException {
    public MissingException(String message) {
        super(message);
    }
}