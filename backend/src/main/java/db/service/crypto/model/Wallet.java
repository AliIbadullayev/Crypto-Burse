package db.service.crypto.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="wallet")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wallet {
    @Id
    private String address;

    @Column
    private double amount;

    @Column
    private String cryptoName;



    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_login")
    private Client client;

}
