package db.service.crypto.repository;

import db.service.crypto.model.Crypto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoRepository extends JpaRepository<Crypto,String> {

    public Crypto findByName(String name);
}
