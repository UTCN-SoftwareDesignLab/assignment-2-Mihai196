package service.book;

import model.validation.Notification;
import org.springframework.stereotype.Service;

public interface BookService {

    Notification<Boolean> addBook(String title,String author,String genre,int price,int quantity);
}
