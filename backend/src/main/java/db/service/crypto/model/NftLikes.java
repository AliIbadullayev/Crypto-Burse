package db.service.crypto.model;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="nft_likes")
@Data
public class NftLikes {

    @EmbeddedId
    private NftLikesId pk;

    private boolean liked;




}
