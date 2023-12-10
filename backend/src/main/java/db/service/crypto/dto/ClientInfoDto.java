package db.service.crypto.dto;

import db.service.crypto.model.Client;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientInfoDto {

    private String userLogin;
    private String name;
    private String surname;
    private double fiatBalance;

    public static ClientInfoDto fromUser(Client client){
        return ClientInfoDto.builder()
                .userLogin(client.getUserLogin())
                .name(client.getName())
                .surname(client.getSurname())
                .fiatBalance(client.getFiatBalance())
                .build();
    }
}
