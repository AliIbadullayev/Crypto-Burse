package db.service.crypto.dto;

import lombok.Data;

@Data
public class FiatDepositDto {

    private String clientLogin;

    private double amount;
}
