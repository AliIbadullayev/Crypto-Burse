package db.service.crypto.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="nft_likes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NftLikes {

    @EmbeddedId
    private NftLikesId pk;

    private boolean liked;




}
