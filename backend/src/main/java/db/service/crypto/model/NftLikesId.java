package db.service.crypto.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@Data
public class NftLikesId implements Serializable {


    public NftLikesId(){

    }


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client")
    private Client client;



    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nft_entity_id")
    private NftEntity nftEntity;


    public NftLikesId(Client client, NftEntity nftEntity) {
        this.client = client;
        this.nftEntity = nftEntity;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
