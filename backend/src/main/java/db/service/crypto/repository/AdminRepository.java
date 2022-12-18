package db.service.crypto.repository;

import db.service.crypto.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin,String> {


}
