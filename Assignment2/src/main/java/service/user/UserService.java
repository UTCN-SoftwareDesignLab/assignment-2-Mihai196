package service.user;

import model.User;
import model.validation.Notification;

import java.util.List;

public interface UserService {

    Notification<Boolean> addUser(String username,String password,String role);
    List<User> findByUsername(String username);
}
