package db.service.crypto;

import db.service.crypto.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CryptoApplication {

//    TODO: проверить нужны ли synchronized в программ
//    TODO: сделать автозаполнение таблиц crypto и blockchain_netwrok при запуске
//    TODO: сделать ограничения целостности в БД

    public static void main(String[] args) {
        SpringApplication.run(CryptoApplication.class, args);
    }


}
