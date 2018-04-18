package controller;

import model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import service.book.BookService;

import java.util.List;

@Controller
public class EmployeeController {

    @Autowired
    private BookService bookService;

    @RequestMapping(value="/employee",method = RequestMethod.GET)
    public String showEmployee()
    {
        return "employee";
    }
    @RequestMapping(value="/employee",params = "findTitle",method=RequestMethod.POST)
    public String findByTitle(Model model, @RequestParam("title") String title)
    {
        List<Book> books=bookService.findByTitle(title);
        String booksss="";
        for(Book b:books)
        {
            booksss+=b.toString()+System.lineSeparator();
        }
        model.addAttribute("typeTable","Books by title");
        model.addAttribute("books", books);
        return "employee";
    }
    @RequestMapping(value="/employee",params="findAuthor",method = RequestMethod.POST)
    public String findByAuthor(Model model,@RequestParam("author") String author)
    {
        List<Book> books=bookService.findByAuthor(author);
        String booksss="";
        for(Book b:books)
        {
            booksss+=b.toString()+System.lineSeparator();
        }
        model.addAttribute("typeTable","Books by author");
        model.addAttribute("books", books);
        return "employee";

    }
    @RequestMapping(value="/employee",params = "findGenre",method = RequestMethod.POST)
    public String findByGenre(Model model,@RequestParam("genre") String genre)
    {
        List<Book> books=bookService.findByGenre(genre);
        String booksss="";
        for(Book b:books)
        {
            booksss+=b.toString()+System.lineSeparator();
        }
        model.addAttribute("typeTable","Books by genre");
        model.addAttribute("books", books);
        return "employee";
    }
}
