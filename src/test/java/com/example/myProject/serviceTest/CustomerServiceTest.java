package com.example.myProject.serviceTest;

import com.example.myProject.dto.common.CustomerResponseDTO;
import com.example.myProject.dto.create.CustomerCreateDTO;
import com.example.myProject.dto.update.CustomerUpdateDTO;
import com.example.myProject.entity.Customer;
import com.example.myProject.repository.CustomerRepository;
import com.example.myProject.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class CustomerServiceTest {

    // Поднимаем PostgreSQL в Docker-контейнере
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:13.16"))
            .withDatabaseName("for_testing")
            .withUsername("for_testing")
            .withPassword("root");

    @DynamicPropertySource
    static void configureTestDatabase(DynamicPropertyRegistry registry) {
        postgres.start();
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll(); // Чистим базу перед каждым тестом

        customer = new Customer(null, "John", "Doe", "john.doe@example.com", "1234567890");
        customer = customerRepository.save(customer); // Сохраняем тестового клиента
    }

    @Test
    void testGetCustomer_Success() {
        CustomerResponseDTO result = customerService.getCustomer(customer.getId());

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getLastName()).isEqualTo("Doe");
    }

    @Test
    void testGetCustomer_NotFound() {
        Exception exception = catchThrowableOfType(() -> customerService.getCustomer(999L), RuntimeException.class);
        assertThat(exception).hasMessage("Customer not found");
    }

    @Test
    void testCreateCustomer() {
        CustomerCreateDTO createDTO = new CustomerCreateDTO("Alice", "Smith", "alice@example.com", "1122334455");
        CustomerResponseDTO result = customerService.createCustomer(createDTO);

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("Alice");

        Optional<Customer> savedCustomer = customerRepository.findById(result.getId());
        assertThat(savedCustomer).isPresent();
    }

    @Test
    void testUpdateCustomer_Success() {
        CustomerUpdateDTO updateDTO = new CustomerUpdateDTO(customer.getId(), "Jane", "Doe", "jane.doe@example.com", "0987654321");
        CustomerResponseDTO result = customerService.updateCustomer(customer.getId(), updateDTO);

        assertThat(result.getFirstName()).isEqualTo("Jane");
        assertThat(result.getEmail()).isEqualTo("jane.doe@example.com");

        Optional<Customer> updatedCustomer = customerRepository.findById(customer.getId());
        assertThat(updatedCustomer).isPresent().get().extracting(Customer::getFirstName).isEqualTo("Jane");
    }

    @Test
    void testUpdateCustomer_NotFound() {
        CustomerUpdateDTO updateDTO = new CustomerUpdateDTO(999L, "Jane", "Doe", "jane.doe@example.com", "0987654321");

        Exception exception = catchThrowableOfType(() -> customerService.updateCustomer(999L, updateDTO), RuntimeException.class);
        assertThat(exception).hasMessage("Customer not found");
    }

    @Test
    void testDeleteCustomer_Success() {
        customerService.deleteCustomer(customer.getId());

        Optional<Customer> deletedCustomer = customerRepository.findById(customer.getId());
        assertThat(deletedCustomer).isEmpty();
    }

    @Test
    void testDeleteCustomer_NotFound() {
        Exception exception = catchThrowableOfType(() -> customerService.deleteCustomer(999L), RuntimeException.class);
        assertThat(exception).hasMessage("Customer not found");
    }
}