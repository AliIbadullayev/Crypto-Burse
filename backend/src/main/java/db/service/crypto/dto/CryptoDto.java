package db.service.crypto.dto;

import db.service.crypto.model.NftEntity;
import lombok.Data;

@Data
public class CryptoDto {
    private String name;
    private double exchangeRate;
}

