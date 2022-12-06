package db.service.crypto.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="client")
@Data
public class Client{

    @Id
    @Column(name="user_login")
    private String userLogin;

    @OneToOne
    @PrimaryKeyJoinColumn()
    private User user;

    @Column
    private String name;

    @Column
    private String surname;

    @Column
    private double fiatBalance;




}
