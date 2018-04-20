import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.books.Books;
import com.google.api.services.books.BooksRequestInitializer;
import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;


public class SampleSearch  {

    private static final String APPLICATION_NAME = "gigi/1.0";
    private static final String API_KEY = "AIzaSyA1U9cNorZhINqI2g6v3fDeLMgnYyFB1WU";
    public static void main(String[] args) {
        try {
            SampleSearch sampleSearch=new SampleSearch();
            List<Volume> volumes=sampleSearch.recomendByTitle("The lord of the rings");
            for(Volume volume:volumes) {
                System.out.println(volume.getVolumeInfo().getAuthors().toString());
                System.out.println(volume.getVolumeInfo());
            }

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<Volume> recomendByTitle(String title) throws GeneralSecurityException, IOException {
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        // Set up Books client.
        final Books books = new Books.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, null)
                .setApplicationName(APPLICATION_NAME)
                .setGoogleClientRequestInitializer(new BooksRequestInitializer(API_KEY))
                .build();
        Volumes volumes = books.volumes().list("intitle:" + title).setFilter("ebooks").execute();
        return volumes.getItems();
    }
}

