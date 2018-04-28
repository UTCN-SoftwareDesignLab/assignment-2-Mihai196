package service;

import model.Book;
import model.Sale;
import model.builder.BookBuilder;
import model.builder.SaleBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import repository.book.BookRepository;
import repository.sale.SaleRepository;
import service.book.BookService;
import service.book.BookServiceImpl;
import service.sale.SaleService;
import service.sale.SaleServiceImpl;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)

public class SaleServiceTest {

    SaleService saleService;
    BookService bookService;
    @Mock
    SaleRepository saleRepository;
    @Mock
    BookRepository bookRepository;

    @Before
    public void setup()
    {
        List<Sale> sales=new ArrayList<Sale>();
        saleService=new SaleServiceImpl(saleRepository);
        bookService=new BookServiceImpl(bookRepository);
        Book book=new BookBuilder().setTitle("asvd").setAuthor("svss").setGenre("SF").setPrice(30.2).setQuantity(30).build();
    }
    @Test
    public void addSale()
    {
        long id=1;
        Book book=new BookBuilder().setId(id).setTitle("asvd").setAuthor("svss").setGenre("SF").setPrice(30.2).setQuantity(30).build();
        Assert.assertTrue(saleService.addSale(book,10).getResult());
    }
    @Test
    public void processSale()
    {
        long id=1;
        Book book=new BookBuilder().setId(id).setTitle("asvd").setAuthor("svss").setGenre("SF").setPrice(30.2).setQuantity(30).build();
        long ids=2;
        Sale sale=new SaleBuilder().setId(ids).setBook(book).setQuantity(10).build();
        Assert.assertTrue(bookService.updateBook(book.getId(),book.getTitle(),book.getAuthor(),
                book.getGenre(),book.getPrice(),book.getQuantity()-sale.getQuantity()).getResult());
    }
}
