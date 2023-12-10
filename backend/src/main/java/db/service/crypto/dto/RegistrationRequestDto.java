package db.service.crypto.dto;

import lombok.Data;

@Data
public class RegistrationRequestDto {

    private String username;
    private String password;
    private String name;
    private String surname;

}
