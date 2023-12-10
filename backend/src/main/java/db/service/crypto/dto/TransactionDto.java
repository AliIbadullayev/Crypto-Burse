package db.service.crypto.dto;

import db.service.crypto.model.Transaction;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class TransactionDto {
    private String walletToAddress;
    private String walletFromAddress;
    private String blockchainNetworkName;
    private double amount;
    private Timestamp timestamp;

    public static TransactionDto fromTransaction(Transaction transaction){
        assert transaction.getWalletFrom() != null;
        assert transaction.getWalletTo() != null;
        assert transaction.getBlockchainNetwork() != null;
        return TransactionDto.builder()
                .amount(transaction.getAmount())
                .walletFromAddress(transaction.getWalletFrom().getAddress())
                .walletToAddress(transaction.getWalletTo().getAddress())
                .blockchainNetworkName(transaction.getBlockchainNetwork().getName())
                .timestamp(transaction.getTimestamp())
                .build();
    }
}
