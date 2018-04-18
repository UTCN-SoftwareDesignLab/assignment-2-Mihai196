package service.user;

import model.User;
import model.builder.UserBuilder;
import model.validation.Notification;
import model.validation.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.user.UserRepository;

import java.security.MessageDigest;
import java.util.List;
import java.util.Optional;

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

    @Override
    public Notification<Boolean> updateUser(Long id, String username, String password, String role) {
        Optional<User> userOptional=userRepository.findById(id);
        User user=new User();
        if (userOptional.isPresent())
        {
            user=userOptional.get();
        }
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
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

    @Override
    public List<User> findByUsername(String username) {
        List<User> users=userRepository.findByUsername(username);
        if (users.isEmpty())
        {
            return null;
        }
        else
        {
            return users;
        }
    }

    @Override
    public List<User> findAll() {
        List<User> users=userRepository.findAll();
        return users;
    }

    @Override
    public void deleteUser(Long id) {
        User user=new UserBuilder().setId(id).build();
        userRepository.delete(user);

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
