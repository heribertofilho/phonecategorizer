package jumia.heriberto.phonecategorizer.customer;

import jumia.heriberto.phonecategorizer.customer.model.dto.CustomerDTO;
import jumia.heriberto.phonecategorizer.customer.exception.CountryCodeNotFoundException;
import jumia.heriberto.phonecategorizer.customer.model.Customer;
import jumia.heriberto.phonecategorizer.customer.model.dto.CountryPhoneEnum;
import jumia.heriberto.phonecategorizer.customer.model.dto.CustomerPageDTO;
import jumia.heriberto.phonecategorizer.customer.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static jumia.heriberto.phonecategorizer.customer.model.dto.CountryPhoneEnum.UNKNOWN;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository repository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final HashMap<Long, CountryPhoneEnum> countryPhoneMap = new HashMap<>();
    private final HashMap<Long, Boolean> validPhoneMap = new HashMap<>();

    public CustomerPageDTO getAll(CountryPhoneEnum country, Boolean onlyValidPhones, int page, int size) {
        Page<Customer> customersPage = repository.findAll(PageRequest.of(page, size));
        Stream<Customer> customers = customersPage.stream().parallel();

        if (country != null) {
            customers = filterByCountry(customers, country);
        }

        if (onlyValidPhones) {
            customers = filterByValidPhone(customers);
        }

        boolean pageWasFiltered = country != null || onlyValidPhones;
        return parsePageToDto(customersPage, customers, pageWasFiltered);
    }

    private Stream<Customer> filterByCountry(Stream<Customer> customers, CountryPhoneEnum country) {
        Pattern pattern = Pattern.compile(String.format("\\(%s\\) .*", country.getCode()));
        return customers.filter(customer -> {
            boolean countryFound = pattern.matcher(customer.getPhone()).find();
            if (countryFound) {
                countryPhoneMap.putIfAbsent(customer.getId(), country);
            }

            return countryFound;
        });
    }

    private Stream<Customer> filterByValidPhone(Stream<Customer> customerStream) {
        return customerStream.filter(customer -> {
            try {
                CountryPhoneEnum country = CountryHelper.getCountry(customer.getPhone());
                boolean isValid = Pattern.compile(country.getRegex()).matcher(customer.getPhone()).find();

                countryPhoneMap.putIfAbsent(customer.getId(), country);
                validPhoneMap.put(customer.getId(), isValid);

                return isValid;
            } catch (CountryCodeNotFoundException e) {
                logger.error(String.format("Phone %s do not have a country code to validate", customer.getPhone()));
                return true;
            }
        });
    }

    private CountryPhoneEnum getCustomerCountry(Customer customer) {
        CountryPhoneEnum country = UNKNOWN;
        if (customer.getPhone() != null) {
            country = countryPhoneMap.get(customer.getId());
            if (country == null) {
                try {
                    country = CountryHelper.getCountry(customer.getPhone());
                } catch (CountryCodeNotFoundException e) {
                    logger.warn(String.format("Customer %s does not has a phone, setting country as %s.", customer.getId(), UNKNOWN));
                    country = UNKNOWN;
                }
            }
        }
        return country;
    }

    private Boolean getCustomerPhoneStatus(Customer customer, CountryPhoneEnum country) {
        Boolean isValid = validPhoneMap.get(customer.getId());
        if (isValid == null) {
            if (country == UNKNOWN) {
                isValid = false;
            } else {
                isValid = Pattern.compile(country.getRegex()).matcher(customer.getPhone()).find();
            }
        }
        return isValid;
    }

    private CustomerPageDTO parsePageToDto(Page<Customer> page, Stream<Customer> stream, boolean pageWasFiltered) {
        List<CustomerDTO> customers = stream.map(this::parseCustomerDTO).collect(Collectors.toList());
        return CustomerPageDTO.builder()
                .totalElementsOnPage(page.getNumberOfElements())
                .totalElementsAfterFilteringPage(customers.size())
                .totalPages(page.getTotalPages())
                .pageWasFiltered(pageWasFiltered)
                .customers(customers)
                .build();
    }

    private CustomerDTO parseCustomerDTO(Customer customer) {
        CountryPhoneEnum country = getCustomerCountry(customer);
        Boolean isValid = getCustomerPhoneStatus(customer, country);

        return CustomerDTO.builder()
                .name(customer.getName())
                .phone(customer.getPhone())
                .country(country)
                .isValidPhoneNumber(isValid)
                .build();
    }
}
