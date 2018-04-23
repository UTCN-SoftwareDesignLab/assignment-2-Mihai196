package service;

import model.Book;
import model.builder.BookBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import repository.book.BookRepository;
import service.book.BookService;
import service.book.BookServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class BookServiceTest {
    @Configuration
    static class BookServiceTestConfig
    {
        @Bean
        public BookRepository bookRepository ()
        {
            return Mockito.mock(BookRepository.class);
        }
        @Autowired
        private BookRepository bookRepository;

        @Bean
        public BookService bookService()
        {
            return new BookServiceImpl(bookRepository);
        }
    }
    @Autowired
    private BookService bookService;
    @Test
    public void saveBook()
    {
        Assert.assertTrue(bookService.addBook("Gigi","GIGI","SF",30.2,10).getResult());
    }
    @Test
    public void updateBook()
    {
        System.out.println(bookService.findAll().size());
        Assert.assertTrue(bookService.findAll().size()==0);
    }




}
