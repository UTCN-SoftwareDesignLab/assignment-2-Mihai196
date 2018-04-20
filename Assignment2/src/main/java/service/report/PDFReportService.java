package service.report;

import model.Book;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.book.BookRepository;

import java.io.IOException;
import java.util.List;

public class PDFReportService implements ReportService {

    @Override
    public void generateReport(List<Book> booksOutOfStock) throws IOException {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);
            PDFont font = PDType1Font.HELVETICA_BOLD;
            try {
                PDPageContentStream contentStream = new PDPageContentStream(document, page);
                String booksToBeAdded = "Books which are out of stock are : ";
                contentStream.beginText();
                contentStream.setFont(font, 12);
                contentStream.setLeading(14.5f);
                contentStream.newLineAtOffset(50, 685);
                contentStream.showText(booksToBeAdded);
                contentStream.newLine();
                for (Book book : booksOutOfStock) {
                    contentStream.showText(book.toString());
                    contentStream.newLine();
                }
                contentStream.endText();
                contentStream.close();
                document.save("Report.pdf");
                document.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
