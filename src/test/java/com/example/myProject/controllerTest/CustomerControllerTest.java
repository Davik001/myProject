package com.example.myProject.controllerTest;


import com.example.myProject.dto.alldtos.CustomerDTO;
import com.example.myProject.dto.common.CustomerResponseDTO;
import com.example.myProject.dto.create.CustomerCreateDTO;
import com.example.myProject.dto.update.CustomerUpdateDTO;
import com.example.myProject.entity.Customer;
import com.example.myProject.repository.CustomerRepository;
import com.example.myProject.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CustomerControllerTest {

    @Container
    private static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:13.16"))
                    .withDatabaseName("for_testing")
                    .withUsername("for_testing")
                    .withPassword("root");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
    }

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    void testCreateCustomer() throws Exception {
        CustomerCreateDTO request = new CustomerCreateDTO("John", "Doe", "john@example.com", "123456789");

        mockMvc.perform(post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void testUpdateCustomer() throws Exception {
        Customer customer = customerRepository.save(new Customer(null, "John", "Doe", "john@example.com", "123456789"));
        CustomerUpdateDTO requestDTO = new CustomerUpdateDTO("John", "Smith", "john.smith@example.com", "987654321");

        mockMvc.perform(put("/customer/{id}", customer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.email").value("john.smith@example.com"));
    }

    @Test
    void testDeleteCustomer() throws Exception {
        Customer customer = customerRepository.save(new Customer(null, "John", "Doe", "john@example.com", "123456789"));

        mockMvc.perform(delete("/customer/{id}", customer.getId()))
                .andExpect(status().isOk());

        assertThat(customerRepository.existsById(customer.getId())).isFalse();
    }

    @Test
    void testGetCustomer() throws Exception {
        Customer customer = customerRepository.save(new Customer(null, "John", "Doe", "john@example.com", "123456789"));

        mockMvc.perform(get("/customer/{id}", customer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void testGetCustomerByEmail() throws Exception {
        Customer customer = customerRepository.save(new Customer(null, "John", "Doe", "john@example.com", "123456789"));

        mockMvc.perform(get("/customer/email")
                        .param("email", "john@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void testGetAllCustomers() throws Exception {
        customerRepository.saveAll(List.of(
                new Customer(null, "John", "Doe", "john@example.com", "123456789"),
                new Customer(null, "Jane", "Smith", "jane@example.com", "987654321")
        ));

        mockMvc.perform(get("/customer")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].firstName").value("John"))
                .andExpect(jsonPath("$.content[1].firstName").value("Jane"));
    }
}