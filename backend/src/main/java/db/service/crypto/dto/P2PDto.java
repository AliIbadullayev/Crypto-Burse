package db.service.crypto.dto;

import db.service.crypto.model.OperationType;
import db.service.crypto.model.P2PTransaction;
import db.service.crypto.model.P2PTransactionStatus;
import lombok.Data;
import org.springframework.security.core.parameters.P;

import java.sql.Timestamp;

@Data
public class P2PDto {

    //Этап 1. В дто только walletOne, cryptoName, cryptoAmount, fiatAmount, operationType
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
        P2PDto p2pDto = new P2PDto();
        p2pDto.setId(p2pTransaction.getId());
        p2pDto.setP2pTransactionStatus(p2pTransaction.getP2pTransactionStatus());
        p2pDto.setCryptoAmount(p2pTransaction.getCryptoAmount());
        p2pDto.setFiatAmount(p2pTransaction.getFiatAmount());
        p2pDto.setOperationType(p2pTransaction.getOperationType());
        p2pDto.setCryptoName(p2pTransaction.getCrypto().getName());
        p2pDto.setWalletOneAddress(p2pTransaction.getWalletOne().getAddress());

        if (p2pTransaction.getWalletTwo() != null)
        p2pDto.setWalletTwoAddress(p2pTransaction.getWalletTwo().getAddress());

        p2pDto.setTimestamp(p2pTransaction.getTimestamp());

        if (p2pTransaction.getAdmin() != null)
        p2pDto.setAdminLogin(p2pTransaction.getAdmin().getUserLogin());
        return p2pDto;
    }

}
