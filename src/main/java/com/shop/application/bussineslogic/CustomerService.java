package com.shop.application.bussineslogic;

import com.shop.application.entities.auctionsdb.Customer;
import com.shop.application.repositories.auctionsdbdao.CustomersRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor

@Service
public class CustomerService{

    private final CustomersRepo repository;

    public List<Customer> getAllCustomers(){
        return repository.findAll();
    }

}
