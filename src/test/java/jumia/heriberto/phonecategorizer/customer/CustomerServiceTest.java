package jumia.heriberto.phonecategorizer.customer;

import jumia.heriberto.phonecategorizer.customer.model.Customer;
import jumia.heriberto.phonecategorizer.customer.model.dto.CountryPhoneEnum;
import jumia.heriberto.phonecategorizer.customer.model.dto.CustomerPageDTO;
import jumia.heriberto.phonecategorizer.customer.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
    @InjectMocks
    private CustomerService service;

    @MockBean
    private CustomerRepository repository;

    @Test
    void validateSuccess_EmptyDatabase() {
        Page<Customer> customerPage = new PageImpl<>(Collections.emptyList());

        when(repository.findAll(any(PageRequest.class))).thenReturn(customerPage);

        CustomerPageDTO pageDTO = service.getAll(null, false, 0, 20);
        assertThat(pageDTO.getCustomers().size()).isEqualTo(0);
        assertThat(pageDTO.getTotalPages()).isEqualTo(1);
        assertThat(pageDTO.getTotalElementsOnPage()).isEqualTo(0);
        assertThat(pageDTO.getTotalElementsAfterFilteringPage()).isEqualTo(0);
    }

    @Test
    void validateSuccess_WithoutFilter() {
        Page<Customer> customerPage = new PageImpl<>(Collections.singletonList(buildCustomer(1L, "John Marston", "(212) 698054317")));

        when(repository.findAll(any(PageRequest.class))).thenReturn(customerPage);

        CustomerPageDTO pageDTO = service.getAll(null, false, 0, 20);
        assertThat(pageDTO.getCustomers().size()).isEqualTo(1);
        assertThat(pageDTO.getTotalPages()).isEqualTo(1);
        assertThat(pageDTO.getTotalElementsOnPage()).isEqualTo(1);
        assertThat(pageDTO.getTotalElementsAfterFilteringPage()).isEqualTo(1);
    }

    @Test
    void validateSuccess_CustomerWithoutPhone() {
        Page<Customer> customerPage = new PageImpl<>(Collections.singletonList(buildCustomer(1L, "John Marston", null)));

        when(repository.findAll(any(PageRequest.class))).thenReturn(customerPage);

        CustomerPageDTO pageDTO = service.getAll(null, false, 0, 20);
        assertThat(pageDTO.getCustomers().size()).isEqualTo(1);
        assertThat(pageDTO.getTotalPages()).isEqualTo(1);
        assertThat(pageDTO.getTotalElementsOnPage()).isEqualTo(1);
        assertThat(pageDTO.getTotalElementsAfterFilteringPage()).isEqualTo(1);

        assertThat(pageDTO.getCustomers().get(0).getCountry()).isEqualTo(CountryPhoneEnum.UNKNOWN);
    }

    @Test
    void validateSuccess_MultipleCustomers() {
        List<Customer> customers = new ArrayList<>();
        customers.add(buildCustomer(1L, "John Marston", "(212) 698054317"));
        customers.add(buildCustomer(2L, "Corvo Attano", "(258) 849181828"));

        Page<Customer> customerPage = new PageImpl<>(customers);

        when(repository.findAll(any(PageRequest.class))).thenReturn(customerPage);

        CustomerPageDTO pageDTO = service.getAll(null, false, 0, 20);
        assertThat(pageDTO.getCustomers().size()).isEqualTo(2);
        assertThat(pageDTO.getTotalPages()).isEqualTo(1);
        assertThat(pageDTO.getTotalElementsOnPage()).isEqualTo(2);
        assertThat(pageDTO.getTotalElementsAfterFilteringPage()).isEqualTo(2);

        assertThat(pageDTO.getCustomers().get(0).getCountry()).isEqualTo(CountryPhoneEnum.MOROCCO);
        assertThat(pageDTO.getCustomers().get(1).getCountry()).isEqualTo(CountryPhoneEnum.MOZAMBIQUE);
    }

    @Test
    void validateSuccess_FilteringByCountry() {
        List<Customer> customers = new ArrayList<>();
        customers.add(buildCustomer(1L, "John Marston", "(212) 698054317"));
        customers.add(buildCustomer(2L, "Corvo Attano", "(258) 849181828"));

        Page<Customer> customerPage = new PageImpl<>(customers);

        when(repository.findAll(any(PageRequest.class))).thenReturn(customerPage);

        CustomerPageDTO pageDTO = service.getAll(CountryPhoneEnum.MOROCCO, false, 0, 20);
        assertThat(pageDTO.getCustomers().size()).isEqualTo(1);
        assertThat(pageDTO.getTotalPages()).isEqualTo(1);
        assertThat(pageDTO.getTotalElementsOnPage()).isEqualTo(2);
        assertThat(pageDTO.getTotalElementsAfterFilteringPage()).isEqualTo(1);

        assertThat(pageDTO.getCustomers().get(0).getCountry()).isEqualTo(CountryPhoneEnum.MOROCCO);
    }

    @Test
    void validateSuccess_FilteringByPhoneState() {
        List<Customer> customers = new ArrayList<>();
        customers.add(buildCustomer(1L, "John Marston", "(212) 6980543175"));
        customers.add(buildCustomer(2L, "Corvo Attano", "(258) 849181828"));

        Page<Customer> customerPage = new PageImpl<>(customers);

        when(repository.findAll(any(PageRequest.class))).thenReturn(customerPage);

        CustomerPageDTO pageDTO = service.getAll(null, true, 0, 20);
        assertThat(pageDTO.getCustomers().size()).isEqualTo(1);
        assertThat(pageDTO.getTotalPages()).isEqualTo(1);
        assertThat(pageDTO.getTotalElementsOnPage()).isEqualTo(2);
        assertThat(pageDTO.getTotalElementsAfterFilteringPage()).isEqualTo(1);

        assertThat(pageDTO.getCustomers().get(0).getName()).isEqualTo(customers.get(1).getName());
    }

    @Test
    void validateSuccess_FilteringByPhoneStateAndCountry() {
        List<Customer> customers = new ArrayList<>();
        customers.add(buildCustomer(1L, "John Marston", "(212) 698054317"));
        customers.add(buildCustomer(2L, "Corvo Attano", "(258) 8491818283"));
        customers.add(buildCustomer(3L, "Master Chief", "(258) 849181828"));

        Page<Customer> customerPage = new PageImpl<>(customers);

        when(repository.findAll(any(PageRequest.class))).thenReturn(customerPage);

        CustomerPageDTO pageDTO = service.getAll(CountryPhoneEnum.MOZAMBIQUE, true, 0, 20);
        assertThat(pageDTO.getCustomers().size()).isEqualTo(1);
        assertThat(pageDTO.getTotalPages()).isEqualTo(1);
        assertThat(pageDTO.getTotalElementsOnPage()).isEqualTo(3);
        assertThat(pageDTO.getTotalElementsAfterFilteringPage()).isEqualTo(1);

        assertThat(pageDTO.getCustomers().get(0).getName()).isEqualTo(customers.get(2).getName());
        assertThat(pageDTO.getCustomers().get(0).getCountry()).isEqualTo(CountryPhoneEnum.MOZAMBIQUE);
        assertThat(pageDTO.getCustomers().get(0).getIsValidPhoneNumber()).isTrue();
    }

    @Test
    void validateSuccess_CustomerWithUnknownPhone() {
        Page<Customer> customerPage = new PageImpl<>(Collections.singletonList(buildCustomer(1L, "Blanka", "055 698054317")));

        when(repository.findAll(any(PageRequest.class))).thenReturn(customerPage);

        CustomerPageDTO pageDTO = service.getAll(null, false, 0, 20);
        assertThat(pageDTO.getCustomers().size()).isEqualTo(1);
        assertThat(pageDTO.getTotalPages()).isEqualTo(1);
        assertThat(pageDTO.getTotalElementsOnPage()).isEqualTo(1);
        assertThat(pageDTO.getTotalElementsAfterFilteringPage()).isEqualTo(1);

        assertThat(pageDTO.getCustomers().get(0).getCountry()).isEqualTo(CountryPhoneEnum.UNKNOWN);
    }

    private Customer buildCustomer(Long id, String name, String phone) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setName(name);
        customer.setPhone(phone);
        return customer;
    }
}
