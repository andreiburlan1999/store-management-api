package com.projects.storemanagement.service;

import com.projects.storemanagement.entity.Customer;

import java.util.List;

public interface CustomerService {

    Customer findById(Long id);
    List<Customer> findAll();
    Customer create(Customer customer);
    Customer update(Long id, Customer customer);

}
