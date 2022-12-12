package db.service.crypto.repository;

import db.service.crypto.model.FiatToCrypto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FiatToCryptoRepository extends JpaRepository<FiatToCrypto,Long> {

}
