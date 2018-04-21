package controller;

import com.google.api.services.books.model.Volume;
import model.Book;
import model.User;
import model.validation.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import service.googleBooksAPI.GoogleSearchService;
import service.book.BookService;
import service.report.ReportFactory;
import service.report.ReportService;
import service.user.UserService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Controller
public class AdminController {

    private BookService bookService;
    private UserService userService;
    private GoogleSearchService googleSearchService;
    private ReportFactory reportFactory;

    @Autowired
    public AdminController(BookService bookService, UserService userService,GoogleSearchService googleSearchService,ReportFactory reportFactory) {
        this.bookService = bookService;
        this.userService = userService;
        this.googleSearchService=googleSearchService;
        this.reportFactory=reportFactory;
    }

    @RequestMapping(value = "/book", params = "update", method = RequestMethod.POST)
    public String updateBook(Model model, @RequestParam("title") String title, @RequestParam("author") String author, @RequestParam("genre") String genre
            , @RequestParam("price") String price, @RequestParam("quantity") String quantity, @RequestParam("IdBookUp") String idBook) {
        double pr = Double.parseDouble(price);
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
    public String addBook(Model model, @ModelAttribute("book") Book book) {
        //System.out.println("Seruuuus ");
        Notification<Boolean> bookNotification = bookService.addBook(book.getTitle(), book.getAuthor(), book.getGenre(), book.getPrice(), book.getQuantity());
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

    @RequestMapping(value = "/book", params = "genReport", method = RequestMethod.POST)
    public String genReportapache(Model model,@RequestParam("typeReport") String reportType) {
        ReportService reportService=reportFactory.getReportType(reportType);
        List<Book> booksOutOfStock=bookService.findByQuantity(0);
        try {
            reportService.generateReport(booksOutOfStock);
            model.addAttribute("addOk", "Report generation was done successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "/book";

    }
    /*@RequestMapping(value = "/book",params = "genReportC",method = RequestMethod.POST)
    public String genCsvReport(Model model)
    {
        try {
            reportService.generateReport("CSV");
            model.addAttribute("addOk", "Report generation was done successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "/book";
    }*/
    @RequestMapping(value = "/userOps", method = RequestMethod.GET)
    public String showUserOps(Model model) {
        if (UserController.getLogggedFlag().equals(Boolean.TRUE)) {
            return "userOps";
        }
        else
        {
            return "redirect:/login";
        }
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
        Long id = Long.parseLong(idUser);
        Notification<Boolean> userNotification = userService.updateUser(id, username, password, role);
        if (userNotification.hasErrors()) {
            model.addAttribute("updateError", userNotification.getFormattedErrors());
            return "/userOps";
        } else {
            model.addAttribute("updateOk", "User was updated successfully");
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

    @RequestMapping(value = "/backToBook", method = RequestMethod.GET)
    public String backToBook() {
        return "redirect:/book";
    }

    @RequestMapping(value = "/book",params = "search",method = RequestMethod.POST)
    public String searchFromApi(Model model,@RequestParam("title")String title)
    {
        try {
            List<Volume> volumes=googleSearchService.findBooksByTitleAPI(title);
            String gigel="";
            for(Volume volume:volumes)
            {
                System.out.println(volume.getSaleInfo().getRetailPrice().getAmount());
                gigel+=volume.getVolumeInfo().getLanguage().toString()+
                        " "+ volume.getVolumeInfo().getTitle().toString()+
                        " "+ volume.getVolumeInfo().getAuthors().get(0).toString()+
                        " "+ volume.getSaleInfo().getRetailPrice().toString()+System.lineSeparator();
            }
            model.addAttribute("bookFetch",gigel);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "/book";
    }

}
