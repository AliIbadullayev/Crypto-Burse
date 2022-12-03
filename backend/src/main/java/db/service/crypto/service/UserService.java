package db.service.crypto.service;

import db.service.crypto.exception.UserAlreadyExistException;
import db.service.crypto.model.User;

import java.util.List;

public interface UserService {

    User register(User user) throws UserAlreadyExistException;

    List<User> getAll();

    User findByUsername(String username);

    void delete(String username);

}
