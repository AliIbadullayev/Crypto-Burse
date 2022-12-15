package db.service.crypto.dto;

import lombok.Data;

@Data
public class FiatToCryptoRequestDto {


    private String walletAddress;

    private double amount;


}
