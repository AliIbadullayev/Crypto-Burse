package db.service.crypto.service;

import db.service.crypto.exception.UserAlreadyExistException;
import db.service.crypto.model.Client;
import db.service.crypto.repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ClientService {


    private final ClientRepository clientRepository;

    private final WalletService walletService;

    @Autowired
    public ClientService(ClientRepository clientRepository, WalletService walletService) {
        this.clientRepository = clientRepository;
        this.walletService = walletService;
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

    public boolean depositFiat(Client client,double amount){
        if (amount > 0 && amount <= 1000000) {
            client.setFiatBalance(client.getFiatBalance()+amount);
            clientRepository.save(client);
            return true;
        } else return false;
    }

}
