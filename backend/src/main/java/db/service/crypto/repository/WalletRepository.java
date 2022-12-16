package db.service.crypto.repository;

import db.service.crypto.model.Client;
import db.service.crypto.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet,String> {
    Wallet findByAddress(String address);

    Wallet findByClientAndCryptoName(Client client, String cryptoName);
}
