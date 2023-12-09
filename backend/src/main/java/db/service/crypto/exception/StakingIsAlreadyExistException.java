package db.service.crypto.exception;

public class StakingIsAlreadyExistException extends RuntimeException {
    public StakingIsAlreadyExistException(String message) {

        super(message);
    }
}
