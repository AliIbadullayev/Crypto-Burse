package db.service.crypto.model;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="blockchain_network")
@Data
public class BlockchainNetwork {


    public BlockchainNetwork(String name, double fee, int leadTime) {
        this.name = name;
        this.fee = fee;
        this.leadTime = leadTime;
    }

    @Id
    private String name;


    private double fee;

    @Column(name = "lead_time")
    private int leadTime;

    public BlockchainNetwork() {

    }
}
