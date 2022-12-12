package db.service.crypto.exception;

public class IllegalWalletPermissionAttemptException extends Exception{
    public IllegalWalletPermissionAttemptException(String message) {
        super(message);
    }
}
