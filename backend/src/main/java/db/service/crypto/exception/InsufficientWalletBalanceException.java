package db.service.crypto.exception;

public class InsufficientWalletBalanceException extends Exception{

    public InsufficientWalletBalanceException(String message) {

        super(message);
    }
}
