package db.service.crypto.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="stacking")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
