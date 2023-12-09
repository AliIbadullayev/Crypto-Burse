package db.service.crypto.exception;

public class SameCryptoInWalletsException extends RuntimeException {

    public SameCryptoInWalletsException(String message) {
        super(message);
    }
}
