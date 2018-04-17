package service.sale;

import model.Book;
import model.validation.Notification;

public interface SaleService {

    Notification<Boolean> addSale(Book book,int quantity);
}
