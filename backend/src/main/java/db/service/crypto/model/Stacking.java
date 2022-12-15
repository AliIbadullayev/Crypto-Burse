package db.service.crypto.model;


import lombok.Cleanup;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="stacking")
@Data
public class Stacking {

    @Id
    @Column(name="wallet_address")
    private String walletAddress;

    @OneToOne
    @PrimaryKeyJoinColumn()
    private Wallet wallet;

    @Column(name="interest_rate")
    private double interestRate;

    private double amount;

    @Column(name="expire_date")
    private Timestamp expireDate;


}
