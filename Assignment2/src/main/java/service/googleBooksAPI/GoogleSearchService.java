package service.googleBooksAPI;

import com.google.api.services.books.model.Volume;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface GoogleSearchService {

    List<Volume> findBooksByTitleAPI(String title) throws GeneralSecurityException, IOException;
}
