package jumia.heriberto.phonecategorizer.customer.exception;

import jumia.heriberto.phonecategorizer.core.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPageSizeException extends BusinessException {
    public InvalidPageSizeException() {
        super("Page size is invalid, please review and try again.", HttpStatus.BAD_REQUEST);
    }
}
