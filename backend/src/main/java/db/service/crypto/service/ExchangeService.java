package db.service.crypto.service;

import db.service.crypto.dto.ExchangeDto;
import db.service.crypto.exception.*;
import db.service.crypto.model.Client;
import db.service.crypto.model.Crypto;
import db.service.crypto.model.CryptoExchange;
import db.service.crypto.model.Wallet;
import db.service.crypto.repository.CryptoRepository;
import db.service.crypto.repository.ExchangeRepository;
import db.service.crypto.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExchangeService {

    private final WalletService walletService;
    private final WalletRepository walletRepository;
    private final CryptoRepository cryptoRepository;
    private final ExchangeRepository exchangeRepository;

    public void makeExchange(ExchangeDto exchangeDto, String ownerUsername) throws WalletNotFoundException, SameCryptoInWalletsException, InsufficientBalanceException, IllegalWalletPermissionAttemptException, InvalidAmountException {
        String addressTo = exchangeDto.getWalletToAddress();
        String addressFrom = exchangeDto.getWalletFromAddress();

        Wallet walletFrom = findByAddress(addressFrom);
        Wallet walletTo = findByAddress(addressTo);

        if (walletTo == null){
            throw new WalletNotFoundException(addressTo);
        }
        if (walletFrom == null){
            throw new WalletNotFoundException(addressFrom);
        }

        double amount = exchangeDto.getAmount();

        if (checkWallets(walletFrom,walletTo,amount,ownerUsername)){
            Crypto cryptoFrom = findByCryptoName(walletFrom.getCryptoName());
            Crypto cryptoTo = findByCryptoName(walletTo.getCryptoName());
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


    private boolean checkWallets(Wallet walletFrom, Wallet walletTo, double amount, String ownerUsername) throws IllegalWalletPermissionAttemptException, SameCryptoInWalletsException, InsufficientBalanceException {
        if (!walletFrom.getClient().getUserLogin().equals(ownerUsername) || !walletTo.getClient().getUserLogin().equals(ownerUsername)){
            throw new IllegalWalletPermissionAttemptException("Оба кошелька должны принадлежать вам!");
        }

        if (walletFrom.getCryptoName().equals(walletTo.getCryptoName())){
            throw new SameCryptoInWalletsException("Кошельки соответствуют одной криптовалюте!");
        }

        if (!checkBalance(walletFrom,amount)) {
            throw new InsufficientBalanceException("Недостаточно средств на балансе кошелька отправителя!");
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

    public List<ExchangeDto> getClientExchanges(Client client) {
        List<CryptoExchange> cryptoExchanges = exchangeRepository.findAll();
        List<ExchangeDto> exchangeDtos = new ArrayList<>();

        for (CryptoExchange cryptoExchange : cryptoExchanges) {
            if (cryptoExchange.getWalletFrom().getClient() == client)
                exchangeDtos.add(ExchangeDto.fromExchange(cryptoExchange));
        }

        return exchangeDtos;

    }

    public List<Crypto> getCryptosExchangeRates(){
        return cryptoRepository.findAll();
    }
}
