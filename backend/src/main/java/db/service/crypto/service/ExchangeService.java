package db.service.crypto.service;

import db.service.crypto.dto.ExchangeRequestDto;
import db.service.crypto.exception.*;
import db.service.crypto.model.Crypto;
import db.service.crypto.model.CryptoExchange;
import db.service.crypto.model.Transaction;
import db.service.crypto.model.Wallet;
import db.service.crypto.repository.CryptoRepository;
import db.service.crypto.repository.ExchangeRepository;
import db.service.crypto.repository.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@Slf4j
public class ExchangeService {

    private final WalletRepository walletRepository;

    private final CryptoRepository cryptoRepository;
    private final ExchangeRepository exchangeRepository;

    private final WalletService walletService;


    @Autowired
    public ExchangeService(WalletRepository walletRepository, CryptoRepository cryptoRepository, ExchangeRepository exchangeRepository, WalletService walletService) {
        this.walletRepository = walletRepository;
        this.cryptoRepository = cryptoRepository;
        this.exchangeRepository = exchangeRepository;
        this.walletService = walletService;
    }


    public void makeExchange(ExchangeRequestDto exchangeRequestDto, String ownerUsername) throws WalletNotFoundException, SameCryptoInWalletsException, InsufficientWalletBalanceException, IllegalSendAttemptException, InvalidAmountException {
        String addressTo = exchangeRequestDto.getWalletToAddress();
        String addressFrom = exchangeRequestDto.getWalletFromAddress();

        Wallet walletFrom = findByAddress(addressFrom);
        Wallet walletTo = findByAddress(addressTo);

        if (walletTo == null){
            throw new WalletNotFoundException(addressTo);
        }
        if (walletFrom == null){
            throw new WalletNotFoundException(addressFrom);
        }

        double amount = exchangeRequestDto.getAmount();

        if (checkWallets(walletFrom,walletTo,amount,ownerUsername)){
            Crypto cryptoFrom = findByCryptoName(walletFrom.getCrypto_name());
            Crypto cryptoTo = findByCryptoName(walletTo.getCrypto_name());
            double amountInFiat = amount * cryptoFrom.getExchange_rate();
            double cryptoToAmount = amountInFiat / cryptoTo.getExchange_rate();
            walletService.withdrawFromWallet(walletFrom,amount);
            walletService.depositWallet(walletTo,cryptoToAmount);
            CryptoExchange cryptoExchange = new CryptoExchange();
            cryptoExchange.setWalletFrom(walletFrom);
            cryptoExchange.setWalletTo(walletTo);
            cryptoExchange.setAmount(amount);
            cryptoExchange.setTimestamp(new Timestamp(System.currentTimeMillis()));
            exchangeRepository.save(cryptoExchange);
        }

    }


    private boolean checkWallets(Wallet walletFrom, Wallet walletTo, double amount, String ownerUsername) throws IllegalSendAttemptException, SameCryptoInWalletsException, InsufficientWalletBalanceException {
        if (!walletFrom.getClient().getUserLogin().equals(ownerUsername) || !walletTo.getClient().getUserLogin().equals(ownerUsername)){
            throw new IllegalSendAttemptException("Оба кошелька должны принадлежать вам!");
        }

        if (walletFrom.getCrypto_name().equals(walletTo.getCrypto_name())){
            throw new SameCryptoInWalletsException("Кошельки соответствуют одной криптовалюте!");
        }

        if (!checkBalance(walletFrom,amount)) {
            throw new InsufficientWalletBalanceException("Недостаточно средств на балансе кошелька отправителя!");
        }
        return true;
    }


    private boolean checkBalance(Wallet walletFrom, double amount){
        return (walletFrom.getAmount() >= amount);
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

}
