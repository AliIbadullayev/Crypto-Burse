package db.service.crypto.model;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="wallet")
@Data
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
