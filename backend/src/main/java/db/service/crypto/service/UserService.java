package db.service.crypto.service;

import db.service.crypto.exception.UserAlreadyExistException;
import db.service.crypto.model.Role;
import db.service.crypto.model.User;
import db.service.crypto.model.Client;
import db.service.crypto.repository.ClientRepository;
import db.service.crypto.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

//TODO проверку на пробелы в нике и пароле (не по краям). Пока работает обычный трим по краям
    public User register(User user) throws UserAlreadyExistException {


        if (findByUsername(user.getUsername())!=null)
        {
            throw new UserAlreadyExistException("Пользователь с таким именем уже зарегистрирован!");
        }

        user.setRole(Role.ROLE_CLIENT);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User registeredUser = userRepository.save(user);

        log.info("IN register - user {} successfully registred", registeredUser);

        return registeredUser;
    }

    public List<User> getAll() {
        List<User> result = userRepository.findAll();
        log.info("IN getAll - {} users found", result.size());
        return null;
    }

    public User findByUsername(String username) {
        User result = null;
        result = userRepository.findByUsername(username);

        if (result == null){
            log.info("IN findByUsername - no user found by username: {}",username);
            return null;
        }

        log.info("IN findByUsername - user: {} found by username: {}",result,username);
        return result;
    }

    public void delete(String username) {
        userRepository.deleteById(username);
        log.info("IN delete - user with username: {} successfully deleted",username);
    }
}
