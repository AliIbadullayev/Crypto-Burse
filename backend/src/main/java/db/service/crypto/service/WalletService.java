package db.service.crypto.service;


import db.service.crypto.dto.FiatToCryptoRequestDto;
import db.service.crypto.exception.*;
import db.service.crypto.model.Crypto;
import db.service.crypto.model.FiatToCrypto;
import db.service.crypto.model.Wallet;
import db.service.crypto.model.Client;
import db.service.crypto.repository.ClientRepository;
import db.service.crypto.repository.CryptoRepository;
import db.service.crypto.repository.FiatToCryptoRepository;
import db.service.crypto.repository.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

@Service
@Slf4j
public class WalletService {

    private final WalletRepository walletRepository;


    private final ClientRepository clientRepository;

    private final CryptoRepository cryptoRepository;

    private final FiatToCryptoRepository fiatToCryptoRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository, ClientRepository clientRepository, CryptoRepository cryptoRepository, FiatToCryptoRepository fiatToCryptoRepository) {
        this.walletRepository = walletRepository;
        this.clientRepository = clientRepository;
        this.cryptoRepository = cryptoRepository;
        this.fiatToCryptoRepository = fiatToCryptoRepository;
    }

    static ArrayList<String> cryptoList = new ArrayList<>(
            Arrays.asList("Bitcoin",
                    "Ethereum",
                    "Litecoin",
                    "Tether",
                    "Shibacoin"));


    public void createWalletsForUser(Client client){

        for (int i = 0; i < cryptoList.size(); i++) {
            Wallet wallet = new Wallet();
            wallet.setAddress(generateId());
            wallet.setCrypto_name(cryptoList.get(i));
            wallet.setClient(client);
            walletRepository.save(wallet);
        }
    }

    public String generateId(){
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 30;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }


    @Transactional
    public void fiatToCrypto(FiatToCryptoRequestDto fiatToCryptoRequestDto, String username) throws WalletNotFoundException, IllegalWalletPermissionAttemptException, CryptoNotFoundException, InvalidAmountException, InsufficientBalanceException {
        String walletAddress = fiatToCryptoRequestDto.getWalletAddress();

        Wallet wallet = findByAddress(walletAddress);

        if (wallet == null) throw new WalletNotFoundException(walletAddress);
        if (!wallet.getClient().getUserLogin().equals(username)) throw new IllegalWalletPermissionAttemptException("Кошелёк должен принадлежать вам!");


        double amount = fiatToCryptoRequestDto.getAmount();
        Crypto crypto = findByCryptoName(wallet.getCrypto_name());

        if (crypto == null) throw new CryptoNotFoundException("Данная криптовалюта не найдена!");

        double amountInCrypto = amount/crypto.getExchange_rate();



        withdrawFiat(username,amount);
        depositWallet(wallet,amountInCrypto);

        FiatToCrypto fiatToCrypto = new FiatToCrypto();

        fiatToCrypto.setWallet(wallet);
        fiatToCrypto.setAmount(amount);
        fiatToCrypto.setTimestamp(new Timestamp(System.currentTimeMillis()));

        fiatToCryptoRepository.save(fiatToCrypto);


    }

    public boolean depositWallet(Wallet wallet,double amount) throws InvalidAmountException {
        if (amount>0){
            double amountBefore = wallet.getAmount();
            double amountAfter = amountBefore+amount;
            wallet.setAmount(amountAfter);
            walletRepository.save(wallet);
            log.info("Deposit wallet with address {}. Amount before: {}. Amount after: {}",wallet.getAddress(),amountBefore,amountAfter);
            return true;
        } else throw new InvalidAmountException("Сумма транзакции не может быть отрицательной");
    }

    public boolean withdrawFromWallet(Wallet wallet, double amount) throws InvalidAmountException {
        if (amount>0){
            double amountBefore = wallet.getAmount();
            double amountAfter = amountBefore-amount;
            wallet.setAmount(amountAfter);
            walletRepository.save(wallet);
            log.info("Withdraw wallet with address {}. Amount before: {}. Amount after: {}",wallet.getAddress(),amountBefore,amountAfter);
            return true;
        } else throw new InvalidAmountException("Сумма транзакции не может быть отрицательной");
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
//
//    //TODO Прокинуть исключение InvalidAmountException в ответ на запрос
//    public boolean depositFiat(Client client,double amount){
//        if (amount > 0 && amount <= 1000000) {
//            client.setFiatBalance(client.getFiatBalance()+amount);
//            clientRepository.save(client);
//            return true;
//        } else return false;
//    }

    public boolean withdrawFiat(String username, double amount) throws InvalidAmountException, InsufficientBalanceException {

        Client client = clientRepository.findByUserLogin(username);


        if (amount > 0){
            System.out.println("Client balance: "+client.getFiatBalance());
            if (client.getFiatBalance()<amount) throw new InsufficientBalanceException("Не достаточно средств на фиатном счёте клиента");
            double amountBefore = client.getFiatBalance();
            double amountAfter = amountBefore - amount;
            client.setFiatBalance(amountAfter);
            clientRepository.save(client);
            log.info("Reduce fiatBalance in client with username {}. Amount before: {}. Amount after: {}",client.getUserLogin(),amountBefore,amountAfter);
            return true;
        } else throw new InvalidAmountException("Сумма транзакции не может быть отрицательной");

    }

}
