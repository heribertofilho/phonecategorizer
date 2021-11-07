package jumia.heriberto.phonecategorizer.customer.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDTO {
    private String name;
    private String phone;
    private CountryPhoneEnum country;
    private Boolean isValidPhoneNumber;
}
