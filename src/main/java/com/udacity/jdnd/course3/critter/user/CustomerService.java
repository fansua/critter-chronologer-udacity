package com.udacity.jdnd.course3.critter.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;


    public Customer save(Customer customer){
        return customerRepository.save(customer);
    }
    public List<Customer> findCustomers(){
        return StreamSupport.stream(customerRepository.findAll().spliterator(),false).collect(Collectors.toList());
    }

    public Customer getCustomerByPetId(Long petId){
         Customer c = customerRepository.findByPetsId(petId);
        return c;
    }
}
