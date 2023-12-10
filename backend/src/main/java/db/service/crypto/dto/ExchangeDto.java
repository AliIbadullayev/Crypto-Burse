package db.service.crypto.dto;

import db.service.crypto.model.CryptoExchange;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class ExchangeDto {

    private String walletToAddress;
    private String walletFromAddress;
    private double amount;
    private Timestamp timestamp;

    public static ExchangeDto fromExchange(CryptoExchange cryptoExchange) {
        assert cryptoExchange.getWalletFrom() != null;
        assert cryptoExchange.getWalletTo() != null;
        return ExchangeDto.builder()
                .amount(cryptoExchange.getAmount())
                .walletFromAddress(cryptoExchange.getWalletFrom().getAddress())
                .walletToAddress(cryptoExchange.getWalletTo().getAddress())
                .timestamp(cryptoExchange.getTimestamp())
                .build();
    }
}
