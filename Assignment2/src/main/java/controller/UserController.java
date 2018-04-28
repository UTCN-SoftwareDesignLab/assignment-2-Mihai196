package controller;

import model.validation.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import service.user.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController implements WebMvcConfigurer {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLogin(Model model) {
        model.addAttribute("incorrect","");
        return "login";
    }


    @RequestMapping(value="/login",method=RequestMethod.POST)
    public String login(Model model, @RequestParam("username")String username, @RequestParam("password") String password,
                        HttpServletRequest request, HttpServletResponse response,BindingResult result) throws ServletException
    {
        try {
            RequestCache requestCache = new HttpSessionRequestCache();
            request.login(username,password);
            SavedRequest savedRequest = requestCache.getRequest(request, response);
            if (savedRequest != null) {
                return "redirect:" + savedRequest.getRedirectUrl();
            } else {
                return "redirect:/login";
            }

        } catch (ServletException authenticationFailed) {
            result.rejectValue(null, "authentication.failed");
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
            return "redirect:/login";
        }
    }

    @RequestMapping(value="/login",params = "createACC",method=RequestMethod.POST)
    public String regg(Model model)
    {
        return "redirect:/registration";

    }
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";
    }
}
