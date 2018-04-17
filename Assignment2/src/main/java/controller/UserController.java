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

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");
        return "login";
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
        }
        else
        {
            System.out.println("The new user was added succesfully to the database");
        }
        return "registration";

    }
}
