package controller;

import model.Book;
import model.Sale;
import model.validation.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import service.book.BookService;
import service.sale.SaleService;

import java.util.List;

@Controller
public class EmployeeController {

    private BookService bookService;
    private SaleService saleService;

    @Autowired
    public EmployeeController(BookService bookService, SaleService saleService) {
        this.bookService = bookService;
        this.saleService = saleService;
    }


    @RequestMapping(value = "/employee", method = RequestMethod.GET)
    public String showEmployee() {
        return "employee";
    }

    @RequestMapping(value = "/employee", params = "findTitle", method = RequestMethod.POST)
    public String findByTitle(Model model, @RequestParam("title") String title) {
        List<Book> books = bookService.findByTitle(title);
        model.addAttribute("typeTable", "Books by title");
        model.addAttribute("books", books);
        return "employee";
    }

    @RequestMapping(value = "/employee", params = "findAuthor", method = RequestMethod.POST)
    public String findByAuthor(Model model, @RequestParam("author") String author) {
        List<Book> books = bookService.findByAuthor(author);
        model.addAttribute("typeTable", "Books by author");
        model.addAttribute("books", books);
        return "employee";

    }

    @RequestMapping(value = "/employee", params = "findGenre", method = RequestMethod.POST)
    public String findByGenre(Model model, @RequestParam("genre") String genre) {
        List<Book> books = bookService.findByGenre(genre);
        model.addAttribute("typeTable", "Books by genre");
        model.addAttribute("books", books);
        return "employee";
    }

    @RequestMapping(value = "/employee", params = "addSale", method = RequestMethod.POST)
    public String addSale(Model model, @RequestParam("bookIdSale") String bookIdSale, @RequestParam("quantitySale") String quantitySale) {
        Long id = Long.parseLong(bookIdSale);
        int quantity = Integer.parseInt(quantitySale);
        Book book = bookService.findById(id);

        if (book != null) {
            Notification<Boolean> saleNotification = saleService.addSale(book, quantity);
            if (saleNotification.hasErrors()) {
                model.addAttribute("addSaleError", saleNotification.getFormattedErrors());
                return "/employee";
            } else {
                model.addAttribute("addSaleOk", "A new sale has been added to the database");
                return "/employee";
            }
        } else {
            model.addAttribute("addSaleError", "Book was not found in the database");
            return "/employee";
        }
    }

    @RequestMapping(value = "/employee", params = "viewSale", method = RequestMethod.POST)
    public String viewSales(Model model) {
        final List<Sale> sales = saleService.findAll();
        model.addAttribute("sales", sales);
        return "/employee";
    }

    @RequestMapping(value = "/employee", params = "processSale", method = RequestMethod.POST)
    public String processSale(Model model, @RequestParam("idSale") String idSale) {
        Long id = Long.parseLong(idSale);
        Sale sale = saleService.findById(id);
        if (sale != null) {
            Book book = sale.getBook();

            Notification<Boolean> bookNotification = bookService.updateBook(book.getId(), book.getTitle(), book.getAuthor(),
                    book.getGenre(), book.getPrice(), book.getQuantity() - sale.getQuantity());
            if (bookNotification.hasErrors()) {
                model.addAttribute("processSaleError", bookNotification.getFormattedErrors());
                return "/employee";
            } else {
                model.addAttribute("processSaleOk", "Sale was processed successfully");
                saleService.deleteSale(sale.getId());
                return "/employee";
            }
        } else {
            model.addAttribute("processSaleError", "Sale was not found in the database");
            return "/employee";
        }
    }

    @RequestMapping(value = "/employee", params = "viewAllBooks", method = RequestMethod.POST)
    public String viewallBooks(Model model) {
        final List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return "/employee";
    }
}
