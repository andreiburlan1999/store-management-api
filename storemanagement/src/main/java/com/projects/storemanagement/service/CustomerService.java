package com.projects.storemanagement.service;

import com.projects.storemanagement.entity.Customer;

import java.util.List;

public interface CustomerService {

    List<Customer> findAllCustomers();
    Customer findCustomer(Long id);
    Customer saveCustomer(Customer customer);
    Customer updateCustomer(Customer customer);

}
