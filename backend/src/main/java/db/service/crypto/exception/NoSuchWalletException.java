package db.service.crypto.exception;

public class NoSuchWalletException extends RuntimeException {

    public NoSuchWalletException(String message) {
        super(message);
    }
}
