package service.sale;

import model.Book;
import model.Sale;
import model.builder.SaleBuilder;
import model.validation.Notification;
import model.validation.SaleValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.sale.SaleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SaleServiceImpl implements SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Override
    public Notification<Boolean> addSale(Book book, int quantity) {
        Sale sale=new SaleBuilder().setBook(book).setQuantity(quantity).build();
        SaleValidator saleValidator=new SaleValidator();
        boolean saleValidation=saleValidator.validate(sale);
        Notification<Boolean> saleNotification=new Notification<>();
        if(!saleValidation)
        {
            saleValidator.getErrors().forEach(saleNotification::addError);
            saleNotification.setResult(Boolean.FALSE);
        }
        else
        {
            saleRepository.save(sale);
            saleNotification.setResult(Boolean.TRUE);
        }
        return saleNotification;
    }

    @Override
    public List<Sale> findAll() {
        List<Sale> sales=saleRepository.findAll();
        return sales;
    }

    @Override
    public Sale findById(Long id) {
        Optional<Sale> sale=saleRepository.findById(id);
        if(sale.isPresent())
        {
            Sale s=sale.get();
            return s;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void deleteSale(Long id) {
        Sale sale=new SaleBuilder().setId(id).build();
        saleRepository.delete(sale);
    }
}
