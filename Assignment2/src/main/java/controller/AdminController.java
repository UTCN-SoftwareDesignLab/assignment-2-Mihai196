package controller;

import model.Book;
import model.User;
import model.validation.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import service.book.BookService;
import service.user.UserService;

import java.util.List;

@Controller
public class AdminController {

    private BookService bookService;
    private UserService userService;

    @Autowired
    public AdminController(BookService bookService, UserService userService) {
        this.bookService = bookService;
        this.userService = userService;
    }

    @RequestMapping(value = "/book", method = RequestMethod.GET)
    public String bookie() {
        System.out.println("Ai ajuns aici din user controller");
        return "book";
    }

    @RequestMapping(value = "/book", params = "update", method = RequestMethod.POST)
    public String updateBook(Model model, @RequestParam("title") String title, @RequestParam("author") String author, @RequestParam("genre") String genre
            , @RequestParam("price") String price, @RequestParam("quantity") String quantity, @RequestParam("IdBookUp") String idBook) {
        int pr = Integer.parseInt(price);
        int quant = Integer.parseInt(quantity);
        Long id = Long.parseLong(idBook);
        Notification<Boolean> bookNotification = bookService.updateBook(id, title, author, genre, pr, quant);
        if (bookNotification.hasErrors()) {
            model.addAttribute("updateError", bookNotification.getFormattedErrors());
            return "book";
        } else {
            model.addAttribute("updateOk", "The book was updated successfully");
            return "book";
        }

    }

    @RequestMapping(value = "/book", params = "add", method = RequestMethod.POST)
    public String addBook(Model model, @RequestParam("title") String title, @RequestParam("author") String author, @RequestParam("genre") String genre
            , @RequestParam("price") String price, @RequestParam("quantity") String quantity) {
        System.out.println("Seruuuus ");
        int pr = Integer.parseInt(price);
        int quant = Integer.parseInt(quantity);
        Notification<Boolean> bookNotification = bookService.addBook(title, author, genre, pr, quant);
        if (bookNotification.hasErrors()) {
            model.addAttribute("addError", bookNotification.getFormattedErrors());
            return "book";
        } else {
            model.addAttribute("addOk", "The new book was added succesfully to the database");
            return "book";
        }
    }

    @RequestMapping(value = "/book", params = "opsUser", method = RequestMethod.POST)
    public String goOps(Model model) {
        return "redirect:/userOps";
    }

    @RequestMapping(value = "/userOps", method = RequestMethod.GET)
    public String showUserOps(Model model) {
        return "userOps";
    }

    @RequestMapping(value = "/userOps", params = "viewUser", method = RequestMethod.POST)
    public String viewUsers(Model model) {
        final List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "userOps";
    }

    @RequestMapping(value = "/userOps", params = "addUser", method = RequestMethod.POST)
    public String addEmployee(Model model, @RequestParam("username") String username, @RequestParam("password") String password,
                              @RequestParam("role") String role) {
        Notification<Boolean> userNotification = userService.addUser(username, password, role);
        if (userNotification.hasErrors()) {
            model.addAttribute("error", userNotification.getFormattedErrors());
            return "/userOps";
        } else {
            model.addAttribute("addOk", "New employee added successfully");
            return "/userOps";
        }
    }

    @RequestMapping(value = "/userOps", params = "updateUser", method = RequestMethod.POST)
    public String updateUser(Model model, @RequestParam("idUser") String idUser, @RequestParam("username") String username,
                             @RequestParam("password") String password, @RequestParam("role") String role) {
        Long id=Long.parseLong(idUser);
        Notification<Boolean> userNotification=userService.updateUser(id,username,password,role);
        if(userNotification.hasErrors())
        {
            model.addAttribute("updateError",userNotification.getFormattedErrors());
            return "/userOps";
        }
        else
        {
            model.addAttribute("updateOk","User was updated successfully");
            return "/userOps";
        }

    }

    @RequestMapping(value = "/userOps", params = "deleteUser", method = RequestMethod.POST)
    public String deleteUser(Model model, @RequestParam("idUser") String idUser) {
        Long id = Long.parseLong(idUser);
        userService.deleteUser(id);
        model.addAttribute("deleteMessage", "User with id " + id + " was deleted succesfully from the database");
        return "/userOps";
    }

    @RequestMapping(value = "/deleteBook", method = RequestMethod.GET)
    public String deleteBook(Model model, @RequestParam("IdBook") String idBook) {
        Long id = Long.parseLong(idBook);
        bookService.deleteBook(id);
        model.addAttribute("deleteMessage", "Book with id " + id + " was deleted succesfully from the database");
        return "book";
    }


    @RequestMapping(value = "/viewBook", method = RequestMethod.GET)
    public String viewBooks(Model model) {
        final List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return "book";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout() {
        return "redirect:/login";
    }
    @RequestMapping(value="/backToBook",method = RequestMethod.GET)
    public String backToBook()
    {
        return "redirect:/book";
    }

}
