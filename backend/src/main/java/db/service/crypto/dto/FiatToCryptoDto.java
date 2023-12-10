package db.service.crypto.dto;

import db.service.crypto.model.FiatToCrypto;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class FiatToCryptoDto {

    private String walletAddress;
    private double amount;
    private Timestamp timestamp;

    public static FiatToCryptoDto fromFiatToCrypto(FiatToCrypto fiatToCrypto) {
        assert fiatToCrypto.getWallet() != null;

        return FiatToCryptoDto.builder()
                .amount(fiatToCrypto.getAmount())
                .walletAddress(fiatToCrypto.getWallet().getAddress())
                .timestamp(fiatToCrypto.getTimestamp())
                .build();
    }
}
