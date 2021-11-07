package jumia.heriberto.phonecategorizer.customer.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CustomerPageDTO {
    private List<CustomerDTO> customers;
    private boolean pageWasFiltered;
    private int totalElementsOnPage;
    private int totalElementsAfterFilteringPage;
    private int totalPages;
}
