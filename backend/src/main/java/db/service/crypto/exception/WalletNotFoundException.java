package db.service.crypto.exception;

public class WalletNotFoundException extends Exception{

    public WalletNotFoundException(String address) {
        super("Wallet with address "+address+" not found");
    }
}
