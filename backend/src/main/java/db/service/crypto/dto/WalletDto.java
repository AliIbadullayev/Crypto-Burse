package db.service.crypto.dto;

import db.service.crypto.model.Wallet;
import lombok.Data;

@Data
public class WalletDto {


    private String address;

    private String crypto_name;

    private double amount;


    public static WalletDto fromWallet(Wallet wallet){
        WalletDto walletDto = new WalletDto();
        walletDto.setAddress(wallet.getAddress());
        walletDto.setCrypto_name(wallet.getCrypto_name());
        walletDto.setAmount(wallet.getAmount());
        return walletDto;
    }
}
