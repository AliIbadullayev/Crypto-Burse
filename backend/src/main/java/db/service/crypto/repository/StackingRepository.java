package db.service.crypto.repository;

import db.service.crypto.model.Stacking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StackingRepository extends JpaRepository<Stacking,String> {

}
