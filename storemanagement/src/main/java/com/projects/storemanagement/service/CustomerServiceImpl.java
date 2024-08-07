package com.projects.storemanagement.service;

import com.projects.storemanagement.entity.Customer;
import com.projects.storemanagement.exception.CustomerNotFoundException;
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
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id " + id));
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer create(Customer customer) {
        customer.setId(null);
        return customerRepository.save(customer);
    }

    @Override
    public Customer update(Long id, Customer customer) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException("Customer not found with id " + id);
        }
        customer.setId(id);
        return customerRepository.save(customer);
    }

}
