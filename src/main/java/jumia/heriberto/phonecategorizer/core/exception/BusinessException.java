package jumia.heriberto.phonecategorizer.core.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends Exception {

    private HttpStatus status;

    public BusinessException(String details, HttpStatus status) {
        super(details);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
