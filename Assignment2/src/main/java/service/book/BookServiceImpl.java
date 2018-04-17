package service.book;


import model.Book;
import model.builder.BookBuilder;
import model.validation.BookValidator;
import model.validation.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.book.BookRepository;

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
        return null;

    }

    @Override
    public void deleteBook(Long id) {
        Book book=new BookBuilder().setId(id).build();
        bookRepository.delete(book);
    }
}
