package db.service.crypto.dto;

import lombok.Data;

@Data
public class TransactionRequestDto {
    private String walletToAddress;
    private String walletFromAddress;
    private String blockchainNetworkName;
    private double amount;

}
