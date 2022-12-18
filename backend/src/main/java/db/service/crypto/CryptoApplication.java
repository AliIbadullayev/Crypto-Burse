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
//    TODO: добавить ссылки на картинки в NFT Entity
//    TODO: get запросы
    //TODO: При добавлении стейкинга добавить проверку, есть ли он уже
    //TODO: добавить инвалидный токен исключение в ответ на запрос, чтобы не кидало 500 интернал сервер ерор


    public static void main(String[] args) {
        SpringApplication.run(CryptoApplication.class, args);
    }


}
