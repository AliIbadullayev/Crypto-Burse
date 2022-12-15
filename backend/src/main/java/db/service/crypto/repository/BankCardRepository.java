package db.service.crypto.repository;

import db.service.crypto.model.BankCard;
import db.service.crypto.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankCardRepository extends JpaRepository<BankCard,String> {

    BankCard findByCardNumber(String cardNumber);
}
