package db.service.crypto.dto;

import db.service.crypto.model.CryptoExchange;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ExchangeDto {

    private String walletToAddress;
    private String walletFromAddress;
    private double amount;

    private Timestamp timestamp;

    public static ExchangeDto fromExchange(CryptoExchange cryptoExchange){
        ExchangeDto exchangeDto = new ExchangeDto();
        exchangeDto.setAmount(cryptoExchange.getAmount());
        exchangeDto.setWalletFromAddress(cryptoExchange.getWalletFrom().getAddress());
        exchangeDto.setWalletToAddress(cryptoExchange.getWalletTo().getAddress());
        exchangeDto.setTimestamp(cryptoExchange.getTimestamp());
        return exchangeDto;
    }
}
