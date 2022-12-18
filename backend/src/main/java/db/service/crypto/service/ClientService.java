package db.service.crypto.service;

import db.service.crypto.exception.UserAlreadyExistException;
import db.service.crypto.model.Client;
import db.service.crypto.model.Crypto;
import db.service.crypto.repository.ClientRepository;
import db.service.crypto.repository.CryptoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ClientService {


    private final ClientRepository clientRepository;

    private final WalletService walletService;

    private final CryptoRepository cryptoRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository, WalletService walletService, CryptoRepository cryptoRepository) {
        this.clientRepository = clientRepository;
        this.walletService = walletService;
        this.cryptoRepository = cryptoRepository;
    }


    public Client createClient(Client client) throws UserAlreadyExistException {


        if (findByUsername(client.getUserLogin())!=null)
        {
            throw new UserAlreadyExistException("Пользователь с таким именем уже зарегистрирован!");
        }

        Client registeredClient = clientRepository.save(client);
        walletService.createWalletsForUser(client);

        log.info("IN register - user {} successfully registred", registeredClient);

        return registeredClient;
    }


    public Client findByUsername(String username) {
        Client result = null;
        result = clientRepository.findByUserLogin(username);

        if (result == null){
            log.info("IN findByUsername - no user found by username: {}",username);
            return null;
        }

        log.info("IN findByUsername - user: {} found by username: {}",result,username);
        return result;
    }


    public List<Crypto> getAllCryptos(){
        return cryptoRepository.findAll();
    }







}
