package model.validation;

import model.Book;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookValidator {
    private final List<String> errors;

    private final String[] GENRES=new String[]{"SF","Adventure","Drama","Thriller","N/A"};

    public BookValidator() {
        errors=new ArrayList<String>();
    }

    public List<String> getErrors() {
        return errors;
    }
    public boolean validate(Book b)
    {
        if (b.getAuthor()==null)
        {
            errors.add("Author cannot be empty");
        }
        if (b.getTitle()==null)
        {
            errors.add("Title cannot be empty");
        }
        if(!Arrays.asList(GENRES).contains(b.getGenre()))
        {
            errors.add("Genre not valid. Available genres: SF,Adventure,Drama,Thriller,N/A");
        }
        if (b.getQuantity()<0)
        {
            errors.add("Can't have a negative quantity");
        }
        if(b.getPrice()<0)
        {
            errors.add("Can't have a negative price");
        }
        return errors.isEmpty();
    }
}
