package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import service.book.BookService;

@Controller
public class EmployeeController {

    @Autowired
    private BookService bookService;
}
