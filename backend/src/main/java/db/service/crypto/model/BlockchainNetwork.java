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

    @Id
    private String name;


    private double fee;

    @Column(name = "lead_time")
    private int leadTime;
}
