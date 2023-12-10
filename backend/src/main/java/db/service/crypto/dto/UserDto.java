package db.service.crypto.dto;

import db.service.crypto.model.Role;
import db.service.crypto.model.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {

    private String username;
    private Role role;

    public static UserDto fromUser(User user) {
        return UserDto.builder()
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }
}
