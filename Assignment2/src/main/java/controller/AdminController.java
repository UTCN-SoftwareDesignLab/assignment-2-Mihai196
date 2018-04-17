package controller;

import model.validation.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import service.book.BookService;

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

    @RequestMapping(value="/book",method=RequestMethod.POST)
    public String addBook(@RequestParam ("title") String title, @RequestParam("author") String author, @RequestParam("genre")String genre
            , @RequestParam("price") String price, @RequestParam("quantity") String quantity)
    {
            System.out.println("Seruuuus ");
            int pr = Integer.parseInt(price);
            int quant = Integer.parseInt(quantity);
            System.out.println(title);
            System.out.println(author);
            System.out.println(genre);
            System.out.println(pr);
            System.out.println(quant);
            Notification<Boolean> bookNotification = bookService.addBook(title, author, genre, pr, quant);
            if (bookNotification.hasErrors()) {
                System.out.println(bookNotification.getFormattedErrors());
                return "book";
            } else {
                System.out.println("The new book was added succesfully to the database");
                return "book";
            }
     }

    /*@RequestMapping(value="/book",method=RequestMethod.POST)
    public String deleteBook(@RequestParam ("idBook") String idBook)
    {
        Long id=Long.parseLong(idBook);
        System.out.println(id);
        bookService.deleteBook(id);
        System.out.println("Book was deleted succesfully from the database");
        return "book";

    }*/

}
