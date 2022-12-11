package db.service.crypto.dto;

import lombok.Data;

@Data
public class ExchangeRequestDto {

    private String walletToAddress;
    private String walletFromAddress;
    private double amount;
}
