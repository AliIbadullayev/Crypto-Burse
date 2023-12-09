package db.service.crypto.exception;

public class SameClientException extends RuntimeException {
    public SameClientException(String message) {
        super(message);
    }
}
