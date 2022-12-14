package db.service.crypto.dto;

import db.service.crypto.model.NftEntity;
import lombok.Data;

import javax.crypto.MacSpi;

@Data
public class NftDto {

    private Long id;
    private String name;

    private double price;

    private String url;

    private boolean placed;

    private long likes;

    private long dislikes;

    private String owner;


    public static NftDto fromNft(NftEntity nftEntity,long likes, long dislikes){
        NftDto nftDto = new NftDto();
        nftDto.setId(nftEntity.getId());
        nftDto.setName(nftEntity.getName());
        nftDto.setPrice(nftEntity.getPrice());
        nftDto.setPlaced(nftEntity.isPlaced());
        nftDto.setOwner(nftEntity.getClient().getUserLogin());
        nftDto.setUrl(nftEntity.getUrl());
        nftDto.setLikes(likes);
        nftDto.setDislikes(dislikes);
        return nftDto;
    }

}
