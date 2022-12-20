package db.service.crypto.model;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="crypto")
@Data
public class Crypto {


    public Crypto(String name, double exchange_rate) {
        this.name = name;
        this.exchange_rate = exchange_rate;
    }

    @Id
    @Column
    private String name;

    @Column
    private double exchange_rate;

    public Crypto() {

    }
}
