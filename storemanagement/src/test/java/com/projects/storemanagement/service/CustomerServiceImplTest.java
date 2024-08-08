package com.projects.storemanagement.service;

import com.projects.storemanagement.entity.Customer;
import com.projects.storemanagement.exception.CustomerNotFoundException;
import com.projects.storemanagement.exception.EmailAlreadyExistsException;
import com.projects.storemanagement.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Customer foundCustomer = customerService.findById(1L);

        assertNotNull(foundCustomer);
        assertEquals("John Doe", foundCustomer.getName());
    }

    @Test
    void testFindByIdThrowsException() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.findById(1L));
    }

    @Test
    void testFindAll() {
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setName("John Doe");
        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setName("Jane Smith");
        when(customerRepository.findAll()).thenReturn(List.of(customer1, customer2));

        List<Customer> customers = customerService.findAll();

        assertNotNull(customers);
        assertEquals(2, customers.size());
    }

    @Test
    void testCreate() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("New Customer");
        customer.setEmail("NonExistingEmail");
        when(customerRepository.existsByEmail("NonExistingEmail")).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer createdCustomer = customerService.create(customer);

        assertNotNull(createdCustomer);
        assertEquals("New Customer", createdCustomer.getName());
    }

    @Test
    void testCreateThrowsException() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("New Customer");
        customer.setEmail("NonValidEmail");
        when(customerRepository.existsByEmail("NonValidEmail")).thenReturn(true);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        assertThrows(EmailAlreadyExistsException.class, () -> customerService.create(customer));
    }

    @Test
    void testUpdate() {
        Customer existingCustomer = new Customer();
        existingCustomer.setId(1L);
        existingCustomer.setName("Old Customer");
        existingCustomer.setEmail("OldEmail");
        when(customerRepository.existsById(1L)).thenReturn(true);

        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(1L);
        updatedCustomer.setName("Updated Customer");
        updatedCustomer.setEmail("NewEmail");
        when(customerRepository.existsByEmail(updatedCustomer.getEmail())).thenReturn(false);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));

        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

        Customer result = customerService.update(1L, updatedCustomer);

        assertNotNull(result);
        assertEquals("Updated Customer", result.getName());
    }

    @Test
    void testUpdateWithSameEmail() {
        Customer existingCustomer = new Customer();
        existingCustomer.setId(1L);
        existingCustomer.setName("Old Customer");
        existingCustomer.setEmail("OldEmail");
        when(customerRepository.existsById(1L)).thenReturn(true);

        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(1L);
        updatedCustomer.setName("Updated Customer");
        updatedCustomer.setEmail("OldEmail");
        when(customerRepository.existsByEmail(updatedCustomer.getEmail())).thenReturn(true);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));

        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

        Customer result = customerService.update(1L, updatedCustomer);

        assertNotNull(result);
        assertEquals("Updated Customer", result.getName());
    }

    @Test
    void testUpdateThrowsNonExistingCustomerException() {
        Customer nonExistingCustomer = new Customer();
        nonExistingCustomer.setId(2L);
        nonExistingCustomer.setName("Non-existent Customer");
        when(customerRepository.existsById(2L)).thenReturn(false);

        assertThrows(CustomerNotFoundException.class, () -> customerService.update(2L, nonExistingCustomer));
    }

    @Test
    void testUpdateThrowsExistingEmailException() {
        Customer existingCustomer = new Customer();
        existingCustomer.setId(1L);
        existingCustomer.setName("Old Customer");
        existingCustomer.setEmail("OldEmail");
        when(customerRepository.existsById(1L)).thenReturn(true);

        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(1L);
        updatedCustomer.setName("Updated Customer");
        updatedCustomer.setEmail("ExistingEmail");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.existsByEmail(updatedCustomer.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> customerService.update(1L, updatedCustomer));
    }

}
