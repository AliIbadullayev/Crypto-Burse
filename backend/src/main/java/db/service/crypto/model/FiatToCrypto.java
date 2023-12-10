package db.service.crypto.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="fiat_to_crypto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FiatToCrypto {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wallet")
    private Wallet wallet;

    private double amount;

    private Timestamp timestamp;

}
