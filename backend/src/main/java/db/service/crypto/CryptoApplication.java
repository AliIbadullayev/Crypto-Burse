package db.service.crypto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class CryptoApplication {

//    TODO: проверить нужны ли synchronized в программ
//    TODO: сделать ограничения целостности в БД


    public static void main(String[] args) {
        SpringApplication.run(CryptoApplication.class, args);
    }



}
