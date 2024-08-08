package com.projects.storemanagement.service;

import com.projects.storemanagement.entity.Customer;
import com.projects.storemanagement.exception.CustomerNotFoundException;
import com.projects.storemanagement.exception.EmailAlreadyExistsException;
import com.projects.storemanagement.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Customer findById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer create(Customer customer) {
        String email = customer.getEmail();
        if(customerRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        customer.setId(null);
        return customerRepository.save(customer);
    }

    @Override
    public Customer update(Long id, Customer customer) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException(id);
        }

        Customer existingCustomer = customerRepository.findById(id).get();
        if(isEmailNotValid(existingCustomer.getEmail(), customer.getEmail())) {
            throw new EmailAlreadyExistsException(customer.getEmail());
        }

        customer.setId(id);
        return customerRepository.save(customer);
    }

    private boolean isEmailNotValid(String oldEmail, String newEmail) {
        return (!oldEmail.equals(newEmail)) && customerRepository.existsByEmail(newEmail);
    }

}
