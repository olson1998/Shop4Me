package com.shop.application.repositories.auctionsdbdao;

import com.shop.application.entities.auctionsdb.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomersRepo extends JpaRepository<Customer, Long> {
}
