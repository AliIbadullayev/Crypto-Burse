package db.service.crypto.dto;


import db.service.crypto.model.Stacking;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class StackingRequestDto {

    private int years;

    private String walletAddress;


    private double amount;


}
