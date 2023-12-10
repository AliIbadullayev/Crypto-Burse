package db.service.crypto.dto;

import db.service.crypto.model.OperationType;
import db.service.crypto.model.P2PTransaction;
import db.service.crypto.model.P2PTransactionStatus;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class P2PDto {

    private long id;
    private  String walletOneAddress;
    private String walletTwoAddress;
    private String adminLogin;
    private String cryptoName;
    private double cryptoAmount;
    private double fiatAmount;
    private OperationType operationType;
    private P2PTransactionStatus p2pTransactionStatus;
    private Timestamp timestamp;

    public static P2PDto fromP2PTransaction(P2PTransaction p2pTransaction){
        return P2PDto.builder()
                .id(p2pTransaction.getId())
                .p2pTransactionStatus(p2pTransaction.getP2pTransactionStatus())
                .cryptoAmount(p2pTransaction.getCryptoAmount())
                .fiatAmount(p2pTransaction.getFiatAmount())
                .operationType(p2pTransaction.getOperationType())
                .cryptoName(p2pTransaction.getCrypto().getName())
                .walletOneAddress(p2pTransaction.getWalletOne().getAddress())
                .walletTwoAddress(p2pTransaction.getWalletTwo() != null ? p2pTransaction.getWalletTwo().getAddress() : null)
                .timestamp(p2pTransaction.getTimestamp())
                .adminLogin(p2pTransaction.getAdmin() != null ? p2pTransaction.getAdmin().getUserLogin() : null)
                .build();
    }

}
