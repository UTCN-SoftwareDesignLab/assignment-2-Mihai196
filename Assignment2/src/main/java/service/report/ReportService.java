package service.report;

import model.Book;

import java.io.IOException;
import java.util.List;

public interface ReportService {
    void generateReport(List<Book> booksOutOfStock) throws IOException;
}
