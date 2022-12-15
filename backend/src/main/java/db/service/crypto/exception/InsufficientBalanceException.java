package db.service.crypto.exception;

public class InsufficientBalanceException extends Exception{

    public InsufficientBalanceException(String message) {

        super(message);
    }
}
