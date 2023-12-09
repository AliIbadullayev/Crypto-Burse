package db.service.crypto.service;


import db.service.crypto.dto.NftDto;
import db.service.crypto.dto.ScoreNftRequestDto;
import db.service.crypto.exception.*;
import db.service.crypto.model.*;
import db.service.crypto.repository.NftEntityRepository;
import db.service.crypto.repository.NftLikesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NftService {


    private final NftEntityRepository nftEntityRepository;
    private final NftLikesRepository nftLikesRepository;

    private final WalletService walletService;

    private final ClientService clientService;


    private static final int priceChange = 10;
    private static final int minPrice = 100;


    @Autowired
    public NftService(NftEntityRepository nftEntityRepository, NftLikesRepository nftLikesRepository, WalletService walletService, ClientService clientService) {
        this.nftEntityRepository = nftEntityRepository;
        this.nftLikesRepository = nftLikesRepository;
        this.walletService = walletService;
        this.clientService = clientService;
    }


    public NftDto scoreNft(ScoreNftRequestDto scoreNftRequestDto, String username) throws NftNotFoundException, NftPlacingException, AlreadyScoredException {

        NftEntity nftEntity = nftEntityRepository.findById(scoreNftRequestDto.getNftId()).orElse(null);

        if (nftEntity == null) throw new NftNotFoundException("Вы пытаетесь оценить несуществующую NFT");


        if (!nftEntity.isPlaced()) throw new NftPlacingException("Данная NFT не размещена на маркетплейсе");

        double currentPrice = nftEntity.getPrice();
        double newPrice;

        if (!scoreNftRequestDto.isLiked()) {
            if ((currentPrice - priceChange) < minPrice) newPrice = minPrice;
            else newPrice = currentPrice - priceChange;
        } else{
            newPrice = currentPrice + priceChange;
        }

        NftLikes currentRecord = nftLikesRepository.findById(new NftLikesId(clientService.findByUsername(username).orElse(null),nftEntity)).orElse(null);

        if (currentRecord != null){
            if (currentRecord.isLiked() == scoreNftRequestDto.isLiked()){
                throw new AlreadyScoredException("Вы не можете поставить одну и ту же оценку больше одного раза");
            }
        }


        nftEntity.setPrice(newPrice);
        nftEntityRepository.save(nftEntity);

        NftLikes nftLikes = new NftLikes();
        nftLikes.setPk(new NftLikesId(clientService.findByUsername(username).orElse(null),nftEntity));
        nftLikes.setLiked(scoreNftRequestDto.isLiked());
        nftLikesRepository.save(nftLikes);

        return NftDto.fromNft(nftEntity,getScores(nftEntity)[0],getScores(nftEntity)[1]);
    }


    public List<NftDto> getAllClientNfts(String clientLogin){
        List<NftEntity> nfts = nftEntityRepository.findAll().stream()
                .filter( a-> a.getClient().getUserLogin().equals(clientLogin))
                .collect(Collectors.toList());

        List<NftDto> result = new ArrayList<>();
        for (NftEntity nft : nfts) {
            long[] scores = getScores(nft);
            result.add(NftDto.fromNft(nft, scores[0], scores[1]));
        }
        return result;
    }



    public List<NftDto> getAllNfts(){
        List<NftEntity> nfts = nftEntityRepository.findAll();
        List<NftDto> nftDtos = new ArrayList<>();

        for (NftEntity nft : nfts) {
            long[] scores;
            if (nft.isPlaced()) {
                scores = getScores(nft);
                nftDtos.add(NftDto.fromNft(nft, scores[0], scores[1]));
            }
        }
        return nftDtos;
    }

    public long[] getScores(NftEntity nft){
        long[] scores = new long[2];
        //likes  == scores[0]
        //dislikes == scores[1]
        List<NftLikes> nftLikesRecords;
        nftLikesRecords = nftLikesRepository.findByPk_NftEntity(nft);

        for (NftLikes nftLikesRecord : nftLikesRecords) {
            if (nftLikesRecord.isLiked()) scores[0]++;
            else scores[1]++;
        }

        return scores;
    }

    public NftDto buyNft(NftDto nftDto, Client client) throws NftPlacingException, NftNotFoundException, NftOwnerException, InvalidAmountException, InsufficientBalanceException {
        NftEntity nftEntity = nftEntityRepository.findById(nftDto.getId()).orElse(null);


        if (nftEntity == null) throw new NftNotFoundException("Нет NFT с таким ID!");

        if (!nftEntity.isPlaced()) throw new NftPlacingException("Вы не можете купить неразмещённую NFT");

        if (nftEntity.getClient() == client) throw new NftOwnerException("NFT уже принадлежит вам!");


        walletService.withdrawFiat(client.getUserLogin(),nftEntity.getPrice());
        nftEntity.setClient(client);
        nftEntity.setPlaced(false);
        nftEntityRepository.save(nftEntity);

        return NftDto.fromNft(nftEntity,getScores(nftEntity)[0],getScores(nftEntity)[1]);
    }

    public NftDto sellNft(NftDto nftDto, Client client) throws NftPlacingException, NftOwnerException, NftNotFoundException {
        NftEntity nftEntity = nftEntityRepository.findById(nftDto.getId()).orElse(null);

        if (nftEntity == null) throw new NftNotFoundException("Нет NFT с таким ID!");

        if (nftEntity.isPlaced()) throw new NftPlacingException("NFT уже размещена на продажу");

        if (nftEntity.getClient() != client) throw new NftOwnerException("NFT не принадлежит вам!");

        nftEntity.setPlaced(true);

        nftEntityRepository.save(nftEntity);

        return NftDto.fromNft(nftEntity,getScores(nftEntity)[0],getScores(nftEntity)[1]);
    }

    public NftDto returnNft(NftDto nftDto, Client client) throws NftNotFoundException, NftPlacingException, NftOwnerException {
        NftEntity nftEntity = nftEntityRepository.findById(nftDto.getId()).orElse(null);


        if (nftEntity == null) throw new NftNotFoundException("Нет NFT с таким ID!");

        if (!nftEntity.isPlaced()) throw new NftPlacingException("NFT итак не размещена!");

        if (nftEntity.getClient() != client) throw new NftOwnerException("NFT не принадлежит вам!");

        nftEntity.setPlaced(false);

        nftEntityRepository.save(nftEntity);

        return NftDto.fromNft(nftEntity,getScores(nftEntity)[0],getScores(nftEntity)[1]);
    }
}
