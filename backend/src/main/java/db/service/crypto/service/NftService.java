package db.service.crypto.service;


import db.service.crypto.dto.ScoreNftRequestDto;
import db.service.crypto.exception.NftIsNotPlacedException;
import db.service.crypto.exception.NftNotFoundException;
import db.service.crypto.model.NftEntity;
import db.service.crypto.model.NftLikes;
import db.service.crypto.model.NftLikesId;
import db.service.crypto.repository.NftEntityRepository;
import db.service.crypto.repository.NftLikesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void scoreNft(ScoreNftRequestDto scoreNftRequestDto, String username) throws NftNotFoundException, NftIsNotPlacedException {

        NftEntity nftEntity = nftEntityRepository.findById(scoreNftRequestDto.getNftId()).orElse(null);

        if (nftEntity == null) throw new NftNotFoundException("Вы пытаетесь оценить несуществующую NFT");


        if (!nftEntity.isPlaced()) throw new NftIsNotPlacedException("Данная NFT не размещена на маркетплейсе");

        double currentPrice = nftEntity.getPrice();
        double newPrice;


        System.out.println("Из лайкд: "+scoreNftRequestDto.isLiked());

        if (!scoreNftRequestDto.isLiked()) {
            if ((currentPrice - priceChange) < minPrice) newPrice = minPrice;
            else newPrice = currentPrice - priceChange;
        } else{
            newPrice = currentPrice + priceChange;
        }

        nftEntity.setPrice(newPrice);
        nftEntityRepository.save(nftEntity);

        NftLikes nftLikes = new NftLikes();
        nftLikes.setPk(new NftLikesId(clientService.findByUsername(username),nftEntity));
        nftLikes.setLiked(scoreNftRequestDto.isLiked());

        nftLikesRepository.save(nftLikes);


    }





}
