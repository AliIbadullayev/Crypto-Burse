package db.service.crypto.model;


import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="crypto_exchange")
@Data
public class CryptoExchange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wallet_from")
    private Wallet walletFrom;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wallet_to")
    private Wallet walletTo;

    private double amount;

    private Timestamp timestamp;


}
