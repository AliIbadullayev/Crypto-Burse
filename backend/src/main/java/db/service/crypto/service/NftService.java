package db.service.crypto.service;


import db.service.crypto.dto.NftDto;
import db.service.crypto.dto.ScoreNftRequestDto;
import db.service.crypto.dto.WalletDto;
import db.service.crypto.exception.AlreadyScoredException;
import db.service.crypto.exception.NftIsNotPlacedException;
import db.service.crypto.exception.NftNotFoundException;
import db.service.crypto.model.*;
import db.service.crypto.repository.NftEntityRepository;
import db.service.crypto.repository.NftLikesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NftService {


    private final NftEntityRepository nftEntityRepository;
    private final NftLikesRepository nftLikesRepository;

    private final ClientService clientService;


    private static final int priceChange = 10;
    private static final int minPrice = 100;


    @Autowired
    public NftService(NftEntityRepository nftEntityRepository, NftLikesRepository nftLikesRepository, ClientService clientService) {
        this.nftEntityRepository = nftEntityRepository;
        this.nftLikesRepository = nftLikesRepository;
        this.clientService = clientService;
    }
//TODO сделать проверку, что один юзер может поставить только один лайк/дизлайк
    public void scoreNft(ScoreNftRequestDto scoreNftRequestDto, String username) throws NftNotFoundException, NftIsNotPlacedException, AlreadyScoredException {

        NftEntity nftEntity = nftEntityRepository.findById(scoreNftRequestDto.getNftId()).orElse(null);

        if (nftEntity == null) throw new NftNotFoundException("Вы пытаетесь оценить несуществующую NFT");


        if (!nftEntity.isPlaced()) throw new NftIsNotPlacedException("Данная NFT не размещена на маркетплейсе");

        double currentPrice = nftEntity.getPrice();
        double newPrice;



        if (!scoreNftRequestDto.isLiked()) {
            if ((currentPrice - priceChange) < minPrice) newPrice = minPrice;
            else newPrice = currentPrice - priceChange;
        } else{
            newPrice = currentPrice + priceChange;
        }

        NftLikes currentRecord = nftLikesRepository.findById(new NftLikesId(clientService.findByUsername(username),nftEntity)).orElse(null);

        if (currentRecord != null){
            if (currentRecord.isLiked() == scoreNftRequestDto.isLiked()){
                throw new AlreadyScoredException("Вы не можете поставить одну и ту же оценку больше одного раза");
            }
        }


        nftEntity.setPrice(newPrice);
        nftEntityRepository.save(nftEntity);



        NftLikes nftLikes = new NftLikes();
        nftLikes.setPk(new NftLikesId(clientService.findByUsername(username),nftEntity));
        nftLikes.setLiked(scoreNftRequestDto.isLiked());
        nftLikesRepository.save(nftLikes);
    }


    public List<NftDto> getAllClientNfts(String clientLogin){
        List<NftDto> allNftDtos = getAllNfts();
        List<NftDto> result = new ArrayList<>();
        for (NftDto nftDto : allNftDtos) {
            if (nftDto.getOwner().equals(clientLogin)) result.add(nftDto);
        }
        return result;
    }


    public List<NftDto> getAllNfts(){
        List<NftEntity> nfts = nftEntityRepository.findAll();
        List<NftDto> nftDtos = new ArrayList<>();

        for (NftEntity nft : nfts) {
            long likes = 0;
            long dislikes = 0;
            List<NftLikes> nftLikesRecords;
            nftLikesRecords = nftLikesRepository.findByPk_NftEntity(nft);

            for (NftLikes nftLikesRecord : nftLikesRecords) {
                if (nftLikesRecord.isLiked()) likes++;
                else dislikes++;
            }
            nftDtos.add(NftDto.fromNft(nft,likes,dislikes));
        }

        return nftDtos;
    }
}
