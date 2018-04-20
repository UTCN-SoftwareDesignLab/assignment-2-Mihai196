package service.googleBooksAPI;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.books.Books;
import com.google.api.services.books.BooksRequestInitializer;
import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
public class GoogleSearchServiceImpl implements GoogleSearchService {

    private static final String APPLICATION_NAME = "gigi/1.0";
    private static final String API_KEY = "AIzaSyA1U9cNorZhINqI2g6v3fDeLMgnYyFB1WU";
    @Override
    public List<Volume> findBooksByTitleAPI(String title) throws GeneralSecurityException, IOException {
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        // Set up Books client.
        final Books books = new Books.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, null)
                .setApplicationName(APPLICATION_NAME)
                .setGoogleClientRequestInitializer(new BooksRequestInitializer(API_KEY))
                .build();
        Volumes volumes = books.volumes().list("intitle:" + title).setFilter("paid-ebooks").execute();
        return volumes.getItems();
    }
}
