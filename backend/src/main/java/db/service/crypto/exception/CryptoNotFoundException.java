package db.service.crypto.exception;

public class CryptoNotFoundException extends RuntimeException {

    public CryptoNotFoundException(String message) {
        super(message);
    }
}
