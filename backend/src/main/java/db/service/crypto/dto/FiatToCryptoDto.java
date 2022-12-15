package db.service.crypto.dto;

import db.service.crypto.model.FiatToCrypto;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class FiatToCryptoDto {


    private String walletAddress;

    private double amount;

    private Timestamp timestamp;

    public static FiatToCryptoDto fromFiatToCrypto(FiatToCrypto fiatToCrypto){
        FiatToCryptoDto fiatToCryptoDto = new FiatToCryptoDto();

        fiatToCryptoDto.setAmount(fiatToCrypto.getAmount());
        fiatToCryptoDto.setWalletAddress(fiatToCrypto.getWallet().getAddress());
        fiatToCryptoDto.setTimestamp(fiatToCrypto.getTimestamp());

        return fiatToCryptoDto;
    }


}
