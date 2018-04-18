package service.book;


import model.Book;
import model.builder.BookBuilder;
import model.validation.BookValidator;
import model.validation.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.book.BookRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Notification<Boolean> addBook(String title, String author, String genre, int price, int quantity) {
        System.out.println("Hello from book service");
        Book book=new BookBuilder().setTitle(title).setAuthor(author).setGenre(genre).setPrice(price).setQuantity(quantity).build();
        System.out.println(book.toString());
        BookValidator bookValidator=new BookValidator();
        boolean bookValidation=bookValidator.validate(book);
        Notification<Boolean> bookNotification=new Notification<>();
        if (!bookValidation)
        {
            bookValidator.getErrors().forEach(bookNotification::addError);
            bookNotification.setResult(Boolean.FALSE);
        }
        else
        {
            bookRepository.save(book);
            bookNotification.setResult(Boolean.TRUE);
        }
        return bookNotification;
    }

    @Override
    public Notification<Boolean> updateBook(Long id, String title, String author, String genre, int price, int quantity) {
        Optional<Book> b=bookRepository.findById(id);
        Book book=new Book();
        if (b.isPresent())
        {
            book=b.get();
        }
        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);
        book.setPrice(price);
        book.setQuantity(quantity);
        BookValidator bookValidator=new BookValidator();
        boolean bookValidation=bookValidator.validate(book);
        Notification<Boolean> bookNotification=new Notification<>();
        if (!bookValidation)
        {
            bookValidator.getErrors().forEach(bookNotification::addError);
            bookNotification.setResult(Boolean.FALSE);
        }
        else
        {
            bookRepository.save(book);
            bookNotification.setResult(Boolean.TRUE);
        }
        return bookNotification;


    }

    @Override
    public void deleteBook(Long id) {
        Book book=new BookBuilder().setId(id).build();
        bookRepository.delete(book);
    }

    @Override
    public List<Book> findAll() {
        List<Book> books=bookRepository.findAll();
        return books;
    }

    @Override
    public List<Book> findByTitle(String title) {
        List<Book> books=bookRepository.findByTitle(title);
        return books;
    }

    @Override
    public List<Book> findByAuthor(String author) {
        List<Book> books=bookRepository.findByAuthor(author);
        return books;
    }

    @Override
    public List<Book> findByGenre(String genre) {
        List<Book> books=bookRepository.findByGenre(genre);
        return books;
    }

}
