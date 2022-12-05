package db.service.crypto.repository;

import db.service.crypto.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client,String> {


}
