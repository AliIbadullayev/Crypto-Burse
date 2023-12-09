package db.service.crypto.exception;

public class StakeIsNotReadyYetException extends RuntimeException {

    public StakeIsNotReadyYetException(String message) {
        super(message);
    }
}
