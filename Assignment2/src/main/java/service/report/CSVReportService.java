package service.report;

import model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.book.BookRepository;
import service.book.BookService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CSVReportService implements ReportService {

    @Override
    public void generateReport(List<Book> booksOutOfStock) throws IOException {
        try {
            PrintWriter pw = new PrintWriter(new File("Report.csv"));
            StringBuilder sb = new StringBuilder();
            sb.append("BookId");
            sb.append(',');
            sb.append("Title");
            sb.append(',');
            sb.append("Author");
            sb.append(',');
            sb.append("Price");
            sb.append('\n');
            for (Book book : booksOutOfStock) {
                sb.append(book.getId());
                sb.append(',');
                sb.append(book.getTitle());
                sb.append(',');
                sb.append(book.getAuthor());
                sb.append(',');
                sb.append(book.getPrice());
                sb.append('\n');
            }
            pw.write(sb.toString());
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
