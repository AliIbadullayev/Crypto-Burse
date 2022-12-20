package db.service.crypto.service;


import db.service.crypto.dto.P2PDto;
import db.service.crypto.exception.*;
import db.service.crypto.model.*;
import db.service.crypto.repository.CryptoRepository;
import db.service.crypto.repository.P2PRepository;
import db.service.crypto.repository.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class P2PService {

    private final WalletRepository walletRepository;

    private final WalletService walletService;


    private final CryptoRepository cryptoRepository;

    private final P2PRepository p2pRepository;



    @Autowired
    public P2PService(WalletRepository walletRepository, WalletService walletService, CryptoRepository cryptoRepository, P2PRepository p2PRepository) {
        this.walletRepository = walletRepository;
        this.walletService = walletService;
        this.cryptoRepository = cryptoRepository;
        this.p2pRepository = p2PRepository;
    }


    //Здесь в дто только walletOne, cryptoName, cryptoAmount, fiatAmount, operationType
    public P2PDto postOffer(P2PDto p2pDto, String username) throws WalletNotFoundException, IllegalWalletPermissionAttemptException, CryptoNotFoundException, InvalidAmountException, InvalidOperationTypeException, NotSameCryptoException, InsufficientBalanceException {
        String walletOneAddress = p2pDto.getWalletOneAddress();

        Wallet wallet = findByAddress(walletOneAddress);

        if (wallet == null) throw new WalletNotFoundException(walletOneAddress);
        if (!wallet.getClient().getUserLogin().equals(username)) throw new IllegalWalletPermissionAttemptException("Кошелёк должен принадлежать вам!");

        Crypto crypto = findByCryptoName(p2pDto.getCryptoName());
        if (crypto == null) throw new CryptoNotFoundException("Данная криптовалюта не найдена!");

        if (!wallet.getCryptoName().equals(crypto.getName())) throw new NotSameCryptoException("Указанная криптовалюта не соответствует кошельку");


        double cryptoAmount = p2pDto.getCryptoAmount();
        double fiatAmount = p2pDto.getFiatAmount();

        if (cryptoAmount <= 0 || fiatAmount <= 0) throw new InvalidAmountException("Количество крипты и/или фиата некорректно!");

        OperationType operationType = null;

        if (p2pDto.getOperationType().equals(OperationType.BUY_CRYPTO)) operationType = OperationType.BUY_CRYPTO;
        if (p2pDto.getOperationType().equals(OperationType.SELL_CRYPTO)) operationType = OperationType.SELL_CRYPTO;

        if (operationType == null) throw new InvalidOperationTypeException("Некорректно указан тип операции");


        if (operationType == OperationType.BUY_CRYPTO){
            walletService.withdrawFiat(username,fiatAmount);
        }else{
            walletService.withdrawFromWallet(wallet,cryptoAmount);
        }


        P2PTransactionStatus p2pTransactionStatus = P2PTransactionStatus.PARTNER_WAITING;
        Timestamp p2pPostTimestamp = new Timestamp(System.currentTimeMillis());

        P2PTransaction p2pTransaction = new P2PTransaction();

        p2pTransaction.setCrypto(crypto);
        p2pTransaction.setCryptoAmount(cryptoAmount);
        p2pTransaction.setFiatAmount(fiatAmount);
        p2pTransaction.setWalletOne(wallet);
        p2pTransaction.setOperationType(operationType);
        p2pTransaction.setTimestamp(p2pPostTimestamp);
        p2pTransaction.setP2pTransactionStatus(p2pTransactionStatus);

        p2pTransaction = p2pRepository.save(p2pTransaction);

        p2pDto.setP2pTransactionStatus(p2pTransactionStatus);
        p2pDto.setTimestamp(p2pPostTimestamp);
        p2pDto.setId(p2pTransaction.getId());
        return p2pDto;

    }

    public P2PDto respondToOffer(P2PDto p2pDto, Client client) throws NoSuchP2POfferException, NoSuchWalletException, InvalidAmountException, InsufficientBalanceException, SameClientException {
        P2PTransaction p2pTransaction = p2pRepository.findById(p2pDto.getId()).orElse(null);

        if (p2pTransaction == null || !p2pTransaction.getP2pTransactionStatus().equals(P2PTransactionStatus.PARTNER_WAITING)) throw new NoSuchP2POfferException("Не найдено предложения с таким ID");

        String cryptoName = p2pTransaction.getCrypto().getName();

        Wallet walletTwo = findByClientAndCryptoName(client,cryptoName);

        if (p2pTransaction.getWalletOne().getClient() == client) throw new SameClientException("Вы не можете принять участие в транзакции, которую сами разместили!");

        if (walletTwo == null) throw new NoSuchWalletException("Кошелёк у этого клиента с нужной криптовалютой не найден");


        System.out.println("Client: "+client);
        if (p2pTransaction.getOperationType() == OperationType.BUY_CRYPTO){
            walletService.withdrawFromWallet(walletTwo,p2pTransaction.getCryptoAmount());
        }else{
            walletService.withdrawFiat(client.getUserLogin(), p2pTransaction.getFiatAmount());
        }

        p2pTransaction.setWalletTwo(walletTwo);
        p2pTransaction.setP2pTransactionStatus(P2PTransactionStatus.ADMIN_WAITING);

        p2pRepository.save(p2pTransaction);
        return P2PDto.fromP2PTransaction(p2pTransaction);
    }

    public List<P2PDto> getAllTransactionsToCheck(){
        List<P2PTransaction> allP2Ps = p2pRepository.findAll();
        List<P2PDto> result = new ArrayList<>();
        for (P2PTransaction p2p : allP2Ps){
            if (p2p.getP2pTransactionStatus().equals(P2PTransactionStatus.ADMIN_WAITING)) {
                P2PDto p2pDto = P2PDto.fromP2PTransaction(p2p);
                result.add(p2pDto);
            }
        }
        return result;
    }

    public List<P2PDto> getAllOffers(String username){
        List<P2PTransaction> p2pTransactions= p2pRepository.findAll();
        List<P2PDto> p2pDtos = new ArrayList<>();

        for (P2PTransaction p2pTransaction : p2pTransactions) {
            String p2pTransactionUsername = p2pTransaction.getWalletOne().getClient().getUserLogin();
            if (p2pTransaction.getP2pTransactionStatus().equals(P2PTransactionStatus.PARTNER_WAITING) && !username.equals(p2pTransactionUsername))
                p2pDtos.add(P2PDto.fromP2PTransaction(p2pTransaction));
        }

        return p2pDtos;
    }


    public Wallet findByAddress(String address){
        Wallet result = null;
        result = walletRepository.findByAddress(address);

        if (result == null){
            log.info("IN findByAddress - no wallet found by walletAddress: {}",address);
            return null;
        }

        log.info("IN findByAddress - wallet: {} found by walletAddress: {}",result,address);
        return result;
    }

    public Wallet findByClientAndCryptoName(Client client,String cryptoName){
        Wallet wallet = walletRepository.findByClientAndCryptoName(client,cryptoName);
        System.out.println(wallet);
        return wallet;
    }

    public Crypto findByCryptoName(String name){
        Crypto result = null;
        result = cryptoRepository.findByName(name);

        if (result == null){
            log.info("IN findByCryptoName - no crypto found by cryptoName: {}",name);
            return null;
        }

        log.info("IN findByCryptoName - crypto: {} found by cryptoName: {}",result,name);
        return result;
    }


    public List<P2PDto> getAllClientP2P(Client client) {
        List<P2PTransaction> p2PTransactions = p2pRepository.findAll();
        List<P2PDto> p2PDtos = new ArrayList<>();

        for (P2PTransaction p2PTransaction : p2PTransactions) {

            if (p2PTransaction.getWalletOne().getClient() == client || (p2PTransaction.getWalletTwo() != null &&(p2PTransaction.getWalletTwo().getClient() == client)))
                p2PDtos.add(P2PDto.fromP2PTransaction(p2PTransaction));
        }

        return p2PDtos;
    }


}
