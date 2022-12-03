package db.service.crypto.dto;

import db.service.crypto.model.Role;
import db.service.crypto.model.User;
import lombok.Data;

@Data
public class UserDto {
    private String username;
    private Role role;

     public User toUser(){
         User user = new User();
         user.setUsername(username);
         user.setRole(role);

         return user;
     }

     public static UserDto fromUser(User user) {
         UserDto userDto = new UserDto();
         userDto.setUsername(user.getUsername());
         userDto.setRole(user.getRole());

         return userDto;
     }
}
