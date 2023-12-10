package db.service.crypto.dto;

import lombok.Data;

@Data
public class StackingRequestDto {
    private int years;
    private String walletAddress;
    private double amount;
}
