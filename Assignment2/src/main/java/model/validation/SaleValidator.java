package model.validation;

import model.Sale;

import java.util.ArrayList;
import java.util.List;

public class SaleValidator {

    private final List<String> errors;

    public SaleValidator() {
        errors=new ArrayList<String>();
    }

    public List<String> getErrors() {
        return errors;
    }
    public boolean validate(Sale sale)
    {
        if (sale.getBook().getQuantity()<sale.getQuantity())
            errors.add("Not enough books on the stock");
        return errors.isEmpty();
    }
}
