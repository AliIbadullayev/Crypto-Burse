package db.service.crypto.exception;

public class JwtTokenIsEmptyException extends RuntimeException {

    public JwtTokenIsEmptyException() {
        super();
    }

    public JwtTokenIsEmptyException(String message) {
        super(message);
    }
}
