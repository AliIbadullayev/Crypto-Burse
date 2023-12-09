package db.service.crypto.exception;

public class StakingNotFoundException extends RuntimeException {

    public StakingNotFoundException(String message) {
        super(message);
    }
}
