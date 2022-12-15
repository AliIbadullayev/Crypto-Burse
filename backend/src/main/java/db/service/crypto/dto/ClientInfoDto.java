package db.service.crypto.dto;


import db.service.crypto.model.Client;
import lombok.Data;

@Data
public class ClientInfoDto {

    private String userLogin;

    private String name;

    private String surname;

    private double fiatBalance;



    public static ClientInfoDto fromUser(Client client){
        ClientInfoDto clientInfoDto = new ClientInfoDto();
        clientInfoDto.setUserLogin(client.getUserLogin());
        clientInfoDto.setName(client.getName());
        clientInfoDto.setSurname(client.getSurname());
        clientInfoDto.setFiatBalance(client.getFiatBalance());

        return clientInfoDto;
    }



}
