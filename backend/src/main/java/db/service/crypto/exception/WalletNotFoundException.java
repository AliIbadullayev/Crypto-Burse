package db.service.crypto.exception;

public class WalletNotFoundException extends RuntimeException {

    public WalletNotFoundException(String address) {
        super("Wallet with address "+address+" not found");
    }
}
