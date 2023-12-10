package db.service.crypto.dto;

import db.service.crypto.model.NftEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NftDto {

    private Long id;
    private String name;
    private double price;
    private String url;
    private boolean placed;
    private long likes;
    private long dislikes;
    private String owner;

    public static NftDto fromNft(NftEntity nftEntity, long likes, long dislikes) {
        assert nftEntity.getClient() != null;

        return NftDto.builder()
                .id(nftEntity.getId())
                .name(nftEntity.getName())
                .price(nftEntity.getPrice())
                .placed(nftEntity.isPlaced())
                .owner(nftEntity.getClient().getUserLogin())
                .url(nftEntity.getUrl())
                .likes(likes)
                .dislikes(dislikes)
                .build();
    }

}
