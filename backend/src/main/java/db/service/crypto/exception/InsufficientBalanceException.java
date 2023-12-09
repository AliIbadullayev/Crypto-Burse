package db.service.crypto.exception;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException(String message) {

        super(message);
    }
}
