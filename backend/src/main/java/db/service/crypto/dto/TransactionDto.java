package db.service.crypto.dto;

import db.service.crypto.model.Transaction;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class TransactionDto {
    private String walletToAddress;
    private String walletFromAddress;
    private String blockchainNetworkName;
    private double amount;
    private Timestamp timestamp;

    public static TransactionDto fromTransaction(Transaction transaction){
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAmount(transaction.getAmount());
        transactionDto.setWalletFromAddress(transaction.getWalletFrom().getAddress());
        transactionDto.setWalletToAddress(transaction.getWalletTo().getAddress());
        transactionDto.setBlockchainNetworkName(transaction.getBlockchainNetwork().getName());
        transactionDto.setTimestamp(transaction.getTimestamp());
        return transactionDto;
    }
}
