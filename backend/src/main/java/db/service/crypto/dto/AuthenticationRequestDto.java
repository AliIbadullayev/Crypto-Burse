package db.service.crypto.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class AuthenticationRequestDto implements Serializable {
    private String username;
    private String password;
}
