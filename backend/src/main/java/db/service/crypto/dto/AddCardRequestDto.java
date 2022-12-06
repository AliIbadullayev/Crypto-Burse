package db.service.crypto.dto;
import lombok.Data;
@Data
public class AddCardRequestDto {
    private String cardNumber;
    private String nameOnCard;
    private String expireDate;
    private String cvv;

    private String clientLogin;


}
