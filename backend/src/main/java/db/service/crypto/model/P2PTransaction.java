package db.service.crypto.model;


import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="p2p_transaction")
@Data
public class P2PTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "admin_login")
    private Admin admin;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "crypto_name")
    private Crypto crypto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wallet_one")
    private Wallet walletOne;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wallet_two")
    private Wallet walletTwo;

    private double cryptoAmount;

    private double fiatAmount;

    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    @Enumerated(EnumType.STRING)
    private P2PTransactionStatus p2pTransactionStatus;

    private Timestamp timestamp;
}
