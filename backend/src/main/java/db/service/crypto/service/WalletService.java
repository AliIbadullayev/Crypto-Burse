package db.service.crypto.service;


import db.service.crypto.model.Wallet;
import db.service.crypto.model.Client;
import db.service.crypto.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
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
        int targetStringLength = 20;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }


}
