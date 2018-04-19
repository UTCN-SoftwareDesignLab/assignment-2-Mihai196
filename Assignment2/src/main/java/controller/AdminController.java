package controller;

import model.Book;
import model.User;
import model.validation.Notification;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import service.book.BookService;
import service.user.UserService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
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

    @RequestMapping(value = "/book", params = "genReport", method = RequestMethod.POST)
    public String genReportapache(Model model) {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        PDFont font = PDType1Font.HELVETICA_BOLD;
        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            List<Book> booksOutOfStock = bookService.findByQuantity(0);//all out of stock books have 0 on quantity
            String booksToBeAdded = "Books which are out of stock are : ";
            contentStream.beginText();
            contentStream.setFont(font, 12);
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(50, 685);
            contentStream.showText(booksToBeAdded);
            contentStream.newLine();
            for (Book book : booksOutOfStock) {
                contentStream.showText(book.toString());
                contentStream.newLine();
            }
            contentStream.endText();
            contentStream.close();
            document.save("Report.pdf");
            document.close();
            model.addAttribute("addOk", "Report generation was done successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "/book";
    }
    @RequestMapping(value = "/book",params = "genReportC",method = RequestMethod.POST)
    public String genCsvReport(Model model)
    {
        try {
            PrintWriter pw = new PrintWriter(new File("Report.csv"));
            StringBuilder sb = new StringBuilder();
            sb.append("BookId");
            sb.append(',');
            sb.append("Title");
            sb.append(',');
            sb.append("Author");
            sb.append(',');
            sb.append("Price");
            sb.append('\n');
            List<Book> booksOutOfStock = bookService.findByQuantity(0);//all out of stock books have 0 on quantity
            for(Book book:booksOutOfStock)
            {
                sb.append(book.getId());
                sb.append(',');
                sb.append(book.getTitle());
                sb.append(',');
                sb.append(book.getAuthor());
                sb.append(',');
                sb.append(book.getPrice());
                sb.append('\n');
            }
            pw.write(sb.toString());
            pw.close();
            model.addAttribute("addOk", "Report generation was done successfully");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "/book";

    }

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

}
