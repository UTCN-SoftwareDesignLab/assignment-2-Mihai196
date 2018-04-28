package service;

import model.Book;
import model.builder.BookBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import repository.book.BookRepository;
import service.book.BookService;
import service.book.BookServiceImpl;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class BookServiceTest {

    BookService bookService;
    @Mock
    BookRepository bookRepository;

    @Before
    public void setup() {
        bookService = new BookServiceImpl(bookRepository);
        List<Book> books = new ArrayList<Book>();
        Book book=new BookBuilder().setTitle("asvd").setAuthor("svss").setGenre("SF").setPrice(30.2).setQuantity(0).build();
        books.add(book);
        Mockito.when(bookRepository.findByQuantity(0)).thenReturn(books);
        Mockito.when(bookRepository.findByTitle("asvd")).thenReturn(books);
    }

    @Test
    public void saveBook() {
        Assert.assertTrue(bookService.addBook("Gigi", "GIGI", "SF", 30.2, 10).getResult());
    }

    @Test
    public void updateBook() {
        Book book=new Book();
        long id=1;
        Assert.assertTrue(bookService.updateBook(id,"Gigi","GIGICA","SF",30.2,10).getResult());
    }
    @Test
    public void findByQuantity()
    {
        List<Book> books=bookService.findByQuantity(0);
        Assert.assertTrue(books.size()==1);
    }
    @Test
    public void findByTitle()
    {
        List<Book> books=bookService.findByTitle("asvd");
        Assert.assertTrue(books.size()==1);
    }


}
