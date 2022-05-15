package com.shop.application.repositories.auctionsdbdao;

import com.shop.application.entities.auctionsdb.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public interface CustomersRepo extends JpaRepository<Customer, Long> {

    Optional<Customer> getCustomerByCustomerID(int customerID);

    default boolean existsCustomerByCustomerBody(Customer customer){
        AtomicBoolean exist = new AtomicBoolean(false);
        findAll().stream()
                .filter(cust -> cust.equals(customer))
                .findFirst()
                .ifPresent(cusy-> exist.set(true));
        return exist.get();
    }

    default int findLastCustomerID(){
        AtomicInteger last_id = new AtomicInteger(0);
        findAll().stream()
                .map(Customer::getCustomerID)
                .max(Comparator.naturalOrder())
                .ifPresent(last_id::set);
        return last_id.get();
    }
}
