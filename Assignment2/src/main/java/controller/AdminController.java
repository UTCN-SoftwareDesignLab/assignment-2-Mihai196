package controller;

import model.Book;
import model.validation.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import service.book.BookService;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private BookService bookService;

    @RequestMapping(value="/book",method=RequestMethod.GET)
    public String bookie()
    {
        System.out.println("Ai ajuns aici din user controller");
        return "book";
    }
    @RequestMapping(value="/book",params = "update",method=RequestMethod.POST)
    public String updateBook(Model model,@RequestParam ("title") String title, @RequestParam("author") String author, @RequestParam("genre")String genre
            , @RequestParam("price") String price, @RequestParam("quantity") String quantity,@RequestParam("IdBookUp") String idBook)
    {
        int pr = Integer.parseInt(price);
        int quant = Integer.parseInt(quantity);
        Long id=Long.parseLong(idBook);
        Notification<Boolean> bookNotification = bookService.updateBook(id,title,author,genre,pr,quant);
        if (bookNotification.hasErrors()) {
            model.addAttribute("updateError",bookNotification.getFormattedErrors());
            return "book";
        } else {
            model.addAttribute("updateOk","The book was updated successfully");
            return "book";
        }

    }
    @RequestMapping(value="/book",params = "add",method=RequestMethod.POST)
    public String addBook(Model model,@RequestParam ("title") String title, @RequestParam("author") String author, @RequestParam("genre")String genre
            , @RequestParam("price") String price, @RequestParam("quantity") String quantity)
    {
            System.out.println("Seruuuus ");
            int pr = Integer.parseInt(price);
            int quant = Integer.parseInt(quantity);
            Notification<Boolean> bookNotification = bookService.addBook(title, author, genre, pr, quant);
            if (bookNotification.hasErrors()) {
                model.addAttribute("addError",bookNotification.getFormattedErrors());
                return "book";
            } else {
                model.addAttribute("addOk","The new book was added succesfully to the database");
                return "book";
            }
     }

    @RequestMapping(value="/deleteBook",method=RequestMethod.GET)
    public String deleteBook(Model model,@RequestParam("IdBook") String idBook)
    {
        Long id=Long.parseLong(idBook);
        bookService.deleteBook(id);
        model.addAttribute("deleteMessage","Book with id"+id+"was deleted succesfully from the database");
        return "book";

    }
    @RequestMapping(value="/viewBook",method=RequestMethod.GET)
    public String viewBooks(Model model)
    {
        final List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return "book";
    }
    @RequestMapping(value="/logout",method=RequestMethod.GET)
    public String logout()
    {
        return "redirect:/login";
    }

}
