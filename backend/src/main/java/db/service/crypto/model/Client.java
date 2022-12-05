package db.service.crypto.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="client")
@Data
public class Client{

    @Id
    @Column(name="user_login")
    private String user_login;

    @OneToOne
    @PrimaryKeyJoinColumn()
    private User user;

    @Column(name="name")
    private String name;

    @Column(name="surname")
    private String surname;

    @Column(name="fiat_balance")
    private double fiat_balance;




}
