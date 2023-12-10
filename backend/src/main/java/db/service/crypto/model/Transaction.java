package db.service.crypto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wallet_from")
    private Wallet walletFrom;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wallet_to")
    private Wallet walletTo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "blockchain_network")
    private BlockchainNetwork blockchainNetwork;


    private double amount;


    private Timestamp timestamp;

}
