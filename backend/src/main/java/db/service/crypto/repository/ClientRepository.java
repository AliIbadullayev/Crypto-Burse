package db.service.crypto.repository;

import db.service.crypto.model.Client;
import db.service.crypto.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client,String> {

    Client findByUserLogin(String userLogin);
}
