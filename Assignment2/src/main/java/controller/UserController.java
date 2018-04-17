package controller;

import model.User;
import model.builder.UserBuilder;
import model.validation.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import service.user.UserService;

import java.security.MessageDigest;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLogin(Model model, String error, String logout) {
        return "login";
    }


    @RequestMapping(value="/login",method=RequestMethod.POST)
    public String login(Model model,@RequestParam("username")String username,@RequestParam("password") String password)
    {
        List<User> usersByUsername=userService.findByUsername(username);
        User firstUser=usersByUsername.get(0);
        System.out.println(firstUser.toString());
        if (encodePassword(password).equals(firstUser.getPassword()))
        {
            //username-ul si parola o fost corect gasite
            if (firstUser.getRole().equals("administrator"))
            {
                return "redirect:/book";
            }
            else
            {
                return "redirect:/book";
            }
        }
        else
        {
            return "login";
        }

    }
    @RequestMapping(value="/registration",method=RequestMethod.GET)
    public String reg()
    {
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String register(Model model,@RequestParam("username") String username,@RequestParam("password")String password,@RequestParam("role") String role) {
        Notification<Boolean> userNotification=userService.addUser(username,password,role);
        if (userNotification.hasErrors())
        {
            model.addAttribute("error",userNotification.getFormattedErrors());
            return "registration";
        }
        else
        {
            System.out.println("The new user was added succesfully to the database");
            return "redirect:/book";
        }
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
