package jumia.heriberto.phonecategorizer.customer.repository;

import jumia.heriberto.phonecategorizer.customer.model.Customer;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {
}