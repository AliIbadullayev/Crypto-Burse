package db.service.crypto.dto;

import db.service.crypto.model.Stacking;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class StackingDto {

    private String walletAddress;
    private double interestRate;
    private double amount;
    private Timestamp expireDate;


    public static StackingDto fromStacking(Stacking stacking) {
        return StackingDto.builder()
                .amount(stacking.getAmount())
                .interestRate(stacking.getInterestRate())
                .expireDate(stacking.getExpireDate())
                .walletAddress(stacking.getWalletAddress())
                .build();
    }

}
