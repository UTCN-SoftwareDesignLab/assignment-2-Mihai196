package service.user;

import model.validation.Notification;

public interface UserService {

    Notification<Boolean> addUser(String username,String password,String role);
}
