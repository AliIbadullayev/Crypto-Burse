package db.service.crypto.repository;


import db.service.crypto.model.P2PTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface P2PRepository extends JpaRepository<P2PTransaction,Long> {
}
