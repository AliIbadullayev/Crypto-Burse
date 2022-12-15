package db.service.crypto.repository;

import db.service.crypto.model.NftEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NftEntityRepository extends JpaRepository<NftEntity, Long> {

}
