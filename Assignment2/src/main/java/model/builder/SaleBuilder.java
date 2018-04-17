package model.builder;

import model.Book;
import model.Sale;

public class SaleBuilder {

    private Sale sale;

    public SaleBuilder setBook(Book book)
    {
        sale.setBook(book);
        return this;
    }
    public SaleBuilder setQuantity(int quantity)
    {
        sale.setQuantity(quantity);
        return this;
    }
    public Sale build()
    {
        return sale;
    }
}
