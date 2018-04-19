package controller;

import model.User;
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

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    private static Boolean logggedFlag=false;

    public static Boolean getLogggedFlag() {
        return logggedFlag;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLogin(Model model, String error, String logout) {
        model.addAttribute("incorrect","");
        return "login";
    }


    @RequestMapping(value="/login",method=RequestMethod.POST)
    public String login(Model model,@RequestParam("username")String username,@RequestParam("password") String password)
    {
        List<User> usersByUsername=userService.findByUsername(username);
        if (usersByUsername!=null) {
            User firstUser=usersByUsername.get(0);
            if (encodePassword(password).equals(firstUser.getPassword())) {
                //username-ul si parola o fost corect gasite
                logggedFlag=Boolean.TRUE;
                if (firstUser.getRole().equals("administrator")) {
                    return "redirect:/book";
                } else {
                    return "redirect:/employee";
                }
            } else {
                model.addAttribute("incorrect", "Invalid password for username");
                return "login";
            }
        }
        else
        {
            model.addAttribute("incorrect","User was not found in the database");
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
            logggedFlag=Boolean.TRUE;
            if (role.equals("administrator")) {
                System.out.println("The new user was added successfully to the database");
                return "redirect:/book";
            }
            else
            {
                return "redirect:/employee";
            }
        }
    }

    @RequestMapping(value="/registrationLog",method=RequestMethod.GET)
    public String deleteBook(Model model)
    {
        return "redirect:/registration";

    }
    @RequestMapping(value = "/book", method = RequestMethod.GET)
    public String bookie() {
        if(logggedFlag.equals(Boolean.TRUE)) {
            //System.out.println("Ai ajuns aici din user controller");
            return "book";
        }
        else
        {
            return "redirect:/login";
        }
    }
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout() {
        logggedFlag=Boolean.FALSE;
        return "redirect:/login";
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
