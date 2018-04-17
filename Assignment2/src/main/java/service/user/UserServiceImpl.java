package service.user;

import model.User;
import model.builder.UserBuilder;
import model.validation.Notification;
import model.validation.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.user.UserRepository;

import java.security.MessageDigest;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Notification<Boolean> addUser(String username, String password, String role) {
        System.out.println("Hello from user service");
        User user = new UserBuilder().setUsername(username).setPassword(password).setRole(role).build();
        UserValidator userValidator=new UserValidator();
        boolean userValidation=userValidator.validate(user);
        Notification<Boolean> userNotification= new Notification<>();
        if(!userValidation)
        {
            userValidator.getErrors().forEach(userNotification::addError);
            userNotification.setResult(Boolean.FALSE);
        }
        else
        {
            user.setPassword(encodePassword(password));
            userRepository.save(user);
            userNotification.setResult(Boolean.TRUE);
        }
        return userNotification;

    }

    private String encodePassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
