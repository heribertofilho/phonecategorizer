package jumia.heriberto.phonecategorizer.customer;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jumia.heriberto.phonecategorizer.customer.exception.InvalidPageException;
import jumia.heriberto.phonecategorizer.customer.exception.InvalidPageSizeException;
import jumia.heriberto.phonecategorizer.customer.model.dto.CountryPhoneEnum;
import jumia.heriberto.phonecategorizer.customer.model.dto.CustomerPageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
@Api("Manages customers")
public class CustomerController {

    @Autowired
    private CustomerService service;

    @GetMapping
    @ApiOperation("Returns all customers")
    public CustomerPageDTO getAll(@RequestParam(required = false) CountryPhoneEnum country,
                                  @RequestParam(defaultValue = "false") Boolean onlyValidPhones,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "20") int size) throws InvalidPageException, InvalidPageSizeException {
        validateRequest(page, size);
        return service.getAll(country, onlyValidPhones, page, size);
    }

    private void validateRequest(int page, int size) throws InvalidPageException, InvalidPageSizeException {
        if (page < 0) {
            throw new InvalidPageException();
        }
        if (size < 0) {
            throw new InvalidPageSizeException();
        }
    }
}
