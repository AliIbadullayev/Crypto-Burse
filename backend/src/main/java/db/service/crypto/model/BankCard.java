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
@Table(name="bank_card")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
