package repository.book;

import model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Long> {

    List<Book> findByTitle(String title);
    List<Book> findByGenre(String genre);
    List<Book> findByAuthor(String author);
    List<Book> findByQuantity(int quantity);


}
