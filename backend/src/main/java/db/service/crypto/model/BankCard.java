package db.service.crypto.model;

import com.sun.istack.NotNull;
import lombok.Cleanup;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="bank_card")
@Data
public class BankCard {

    @Id
    private String cardNumber;

    @Column(name="name_on_card")
    private String nameOnCard;

    @Column(name="expire_date")
    private String expireDate;

    @Column(name="cvv")
    private String cvv;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_login")
    private Client client;
}
