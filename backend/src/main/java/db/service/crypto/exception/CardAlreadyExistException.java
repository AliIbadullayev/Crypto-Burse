package db.service.crypto.exception;

public class CardAlreadyExistException extends RuntimeException {

    public CardAlreadyExistException(String message) {
        super(message);
    }
}
