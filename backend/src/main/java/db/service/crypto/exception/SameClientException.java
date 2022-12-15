package db.service.crypto.exception;

public class SameClientException extends Exception{
    public SameClientException(String message) {
        super(message);
    }
}
