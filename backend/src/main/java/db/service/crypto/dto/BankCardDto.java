package db.service.crypto.dto;

import db.service.crypto.model.BankCard;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class BankCardDto {
    private String cardNumber;
    private String nameOnCard;
    private String expireDate;
    private String cvv;

    public static BankCardDto fromCard(BankCard card){
        return BankCardDto.builder()
                .cardNumber(card.getCardNumber())
                .nameOnCard(card.getNameOnCard())
                .expireDate(card.getExpireDate())
                .cvv(card.getCvv())
                .build();
    }


}
