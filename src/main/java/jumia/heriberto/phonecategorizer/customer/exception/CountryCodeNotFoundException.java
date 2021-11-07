package jumia.heriberto.phonecategorizer.customer.exception;

import jumia.heriberto.phonecategorizer.core.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class CountryCodeNotFoundException extends BusinessException {
    public CountryCodeNotFoundException() {
        super("Country Code was not found, please review them.", HttpStatus.BAD_REQUEST);
    }
}
