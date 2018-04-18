package service.sale;

import model.Book;
import model.Sale;
import model.validation.Notification;

import java.util.List;

public interface SaleService {

    Notification<Boolean> addSale(Book book,int quantity);
    List<Sale> findAll();
    Sale findById(Long id);
    void deleteSale(Long id);
}
