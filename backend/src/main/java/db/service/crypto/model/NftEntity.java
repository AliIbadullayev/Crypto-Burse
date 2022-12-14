package db.service.crypto.model;


import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="nft_entity")
@Data
public class NftEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nft_name")
    private String name;

    private double price;

    private boolean placed;

    private String url;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client")
    private Client client;



}
