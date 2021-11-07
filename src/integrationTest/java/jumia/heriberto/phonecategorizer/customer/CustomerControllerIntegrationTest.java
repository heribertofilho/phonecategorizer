package jumia.heriberto.phonecategorizer.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import jumia.heriberto.phonecategorizer.customer.exception.InvalidPageException;
import jumia.heriberto.phonecategorizer.customer.exception.InvalidPageSizeException;
import jumia.heriberto.phonecategorizer.customer.model.Customer;
import jumia.heriberto.phonecategorizer.customer.model.dto.CountryPhoneEnum;
import jumia.heriberto.phonecategorizer.customer.model.dto.CustomerPageDTO;
import jumia.heriberto.phonecategorizer.customer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository repository;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        repository.save(buildCustomer("John Marston", "(258) 849181828"));
    }

    @Test
    void getAll_Success() throws Exception {
        MvcResult result = mockMvc.perform(get("/customer"))
                .andExpect(status().isOk())
                .andReturn();

        CustomerPageDTO pageDTO = mapper.readValue(result.getResponse().getContentAsString(), CustomerPageDTO.class);
        assertThat(pageDTO.getCustomers().size()).isEqualTo(1);
        assertThat(pageDTO.getTotalPages()).isEqualTo(1);
        assertThat(pageDTO.getTotalElementsOnPage()).isEqualTo(1);
        assertThat(pageDTO.getTotalElementsAfterFilteringPage()).isEqualTo(1);

        assertThat(pageDTO.getCustomers().get(0).getCountry()).isEqualTo(CountryPhoneEnum.MOZAMBIQUE);
    }

    @Test
    void getAll_UnexpectedCountry() throws Exception {
        mockMvc.perform(get("/customer")
                        .param("country", "UNEXPECTED"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getAll_NegativePage() throws Exception {
        mockMvc.perform(get("/customer")
                        .param("page", "-1"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidPageException));
    }

    @Test
    void getAll_NegativeSize() throws Exception {
        mockMvc.perform(get("/customer")
                        .param("size", "-1"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidPageSizeException));
    }

    private Customer buildCustomer(String name, String phone) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setPhone(phone);
        return customer;
    }
}

