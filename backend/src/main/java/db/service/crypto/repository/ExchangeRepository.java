package db.service.crypto.repository;

import db.service.crypto.model.CryptoExchange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRepository extends JpaRepository<CryptoExchange,Long> {
}
