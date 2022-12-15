package db.service.crypto.dto;
import db.service.crypto.model.BankCard;
import lombok.Data;
@Data
public class BankCardDto {
    private String cardNumber;
    private String nameOnCard;
    private String expireDate;
    private String cvv;


    public static BankCardDto fromCard(BankCard card){
        BankCardDto bankCardDto = new BankCardDto();
        bankCardDto.setCardNumber(card.getCardNumber());
        bankCardDto.setNameOnCard(card.getNameOnCard());
        bankCardDto.setExpireDate(card.getExpireDate());
        bankCardDto.setCvv(card.getCvv());
        return bankCardDto;
    }


}
