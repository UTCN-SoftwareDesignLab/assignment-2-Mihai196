package service.book;

import model.Book;
import model.validation.Notification;
import org.springframework.stereotype.Service;

import java.util.List;

public interface BookService {

    Notification<Boolean> addBook(String title,String author,String genre,int price,int quantity);
    Notification<Boolean> updateBook(Long id,String title,String author,String genre,int price,int quantity);
    void deleteBook(Long id);
    List<Book> findAll();
    List<Book> findByTitle(String title);
    List<Book> findByAuthor(String author);
    List<Book> findByGenre(String genre);
}
