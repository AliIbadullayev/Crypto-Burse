package db.service.crypto.dto;

import db.service.crypto.model.Wallet;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WalletDto {

    private String address;
    private String cryptoName;
    private double amount;

    public static WalletDto fromWallet(Wallet wallet) {
        return WalletDto.builder()
                .address(wallet.getAddress())
                .cryptoName(wallet.getCryptoName())
                .amount(wallet.getAmount())
                .build();
    }
}
