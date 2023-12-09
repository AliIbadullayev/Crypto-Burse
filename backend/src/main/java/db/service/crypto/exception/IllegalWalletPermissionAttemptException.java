package db.service.crypto.exception;

public class IllegalWalletPermissionAttemptException extends RuntimeException {
    public IllegalWalletPermissionAttemptException(String message) {
        super(message);
    }
}
