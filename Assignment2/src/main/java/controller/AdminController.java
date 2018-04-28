package controller;

import com.google.api.services.books.model.Volume;
import model.Book;
import model.User;
import model.builder.BookBuilder;
import model.validation.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
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
import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {

    private BookService bookService;
    private UserService userService;
    private GoogleSearchService googleSearchService;
    private ReportFactory reportFactory;

    @Autowired
    public AdminController(BookService bookService, UserService userService, GoogleSearchService googleSearchService, ReportFactory reportFactory) {
        this.bookService = bookService;
        this.userService = userService;
        this.googleSearchService = googleSearchService;
        this.reportFactory = reportFactory;
    }

    @RequestMapping(value = "/book", method = RequestMethod.GET)
    public String bookie(ModelMap modelMap) {
        modelMap.addAttribute("book5", new Book());
        return "book";
    }

    //Book operations

    @RequestMapping(value = "/book", params = "update", method = RequestMethod.POST)
    public String updateBook(Model model, @ModelAttribute("book5") Book book) {
        Notification<Boolean> bookNotification = bookService.updateBook(book.getId(), book.getTitle(),
                book.getAuthor(), book.getGenre(), book.getPrice(), book.getQuantity());
        if (bookNotification.hasErrors()) {
            model.addAttribute("updateError", bookNotification.getFormattedErrors());
            return "book";
        } else {
            model.addAttribute("updateOk", "The book was updated successfully");
            return "book";
        }

    }

    @RequestMapping(value = "/book", params = "add", method = RequestMethod.POST)
    public String addBook(Model model, @ModelAttribute("book5") Book book) {
        System.out.println("Seruuuus ");
        Notification<Boolean> bookNotification = bookService.addBook(book.getTitle(), book.getAuthor(),
                book.getGenre(), book.getPrice(), book.getQuantity());
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
    public String genReportapache(Model model, @RequestParam("typeReport") String reportType) {
        model.addAttribute("book5", new Book());
        ReportService reportService = reportFactory.getReportType(reportType);
        List<Book> booksOutOfStock = bookService.findByQuantity(0);
        try {
            reportService.generateReport(booksOutOfStock);
            model.addAttribute("addOk", "Report generation was done successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "/book";

    }

    @RequestMapping(value = "/deleteBook", method = RequestMethod.GET)
    public String deleteBook(Model model, @RequestParam("IdBook") String idBook) {
        model.addAttribute("book5", new Book());
        Long id = Long.parseLong(idBook);
        bookService.deleteBook(id);
        model.addAttribute("deleteMessage", "Book with id " + id + " was deleted succesfully from the database");
        return "book";
    }


    @RequestMapping(value = "/viewBook", params = "viewBooks", method = RequestMethod.GET)
    public String viewBooks(Model model) {
        model.addAttribute("book5", new Book());
        final List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return "book";
    }

    @RequestMapping(value = "/backToBook", method = RequestMethod.GET)
    public String backToBook() {
        return "redirect:/book";
    }

    //User operations
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

    //Google Api operations
    @RequestMapping(value = "/book", params = "search", method = RequestMethod.POST)
    public String searchFromApi(Model model, @RequestParam("title") String title) {
        model.addAttribute("book5", new Book());
        try {
            List<Volume> volumes = googleSearchService.findBooksByTitleAPI(title);
            String gigel = "";
            long i = 0;
            List<Book> booksies = new ArrayList<Book>();
            for (Volume volume : volumes) {
                gigel += i + " " + volume.getVolumeInfo().getTitle().toString() +
                        " " + volume.getVolumeInfo().getAuthors().get(0).toString() +
                        " " + volume.getSaleInfo().getRetailPrice().toString() + "\n";
                Book book = new BookBuilder().setId(i).setTitle(volume.getVolumeInfo().getTitle()).
                        setAuthor(volume.getVolumeInfo().getAuthors().get(0))
                        .setGenre("N/A")
                        .setPrice(volume.getSaleInfo().getRetailPrice().getAmount())
                        .setQuantity(50).build();
                booksies.add(book);
                i++;
            }
            model.addAttribute("books", booksies);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "/book";
    }

    @RequestMapping(value = "/book", params = "addAPI", method = RequestMethod.POST)
    public String addFromApi(Model model, @RequestParam("apiId") String idd, @RequestParam("title") String title) {
        model.addAttribute("book5", new Book());
        int id = Integer.parseInt(idd);
        try {
            List<Volume> volumes = googleSearchService.findBooksByTitleAPI(title);
            Volume volume = volumes.get(id);
            Notification<Boolean> bookNotification = bookService.addBook(volume.getVolumeInfo().getTitle(),
                    volume.getVolumeInfo().getAuthors().get(0),
                    "N/A", volume.getSaleInfo().getRetailPrice().getAmount(), 50);
            if (bookNotification.hasErrors()) {
                model.addAttribute("addError", bookNotification.getFormattedErrors());
            } else {
                model.addAttribute("addOk", "The new book was added succesfully to the database");
            }
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "book";
    }

}
