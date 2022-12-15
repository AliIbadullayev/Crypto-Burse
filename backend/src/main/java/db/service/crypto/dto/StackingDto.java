package db.service.crypto.dto;

import db.service.crypto.model.Stacking;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class StackingDto {

    private String walletAddress;

    private double interestRate;

    private double amount;

    private Timestamp expireDate;


    public static StackingDto fromStacking(Stacking stacking){
        StackingDto stackingDto = new StackingDto();
        stackingDto.setAmount(stacking.getAmount());
        stackingDto.setInterestRate(stacking.getInterestRate());
        stackingDto.setExpireDate(stacking.getExpireDate());
        stackingDto.setWalletAddress(stacking.getWalletAddress());
        return stackingDto;
    }

}
