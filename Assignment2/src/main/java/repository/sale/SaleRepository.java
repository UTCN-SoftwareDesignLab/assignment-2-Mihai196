package repository.sale;

import model.Sale;
import org.springframework.data.repository.CrudRepository;

public interface SaleRepository extends CrudRepository<Sale,Long> {
}
