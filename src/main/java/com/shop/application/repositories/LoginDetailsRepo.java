package com.shop.application.repositories;

import com.shop.application.entities.LoginDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Repository
public interface LoginDetailsRepo extends JpaRepository<LoginDetails, Long> {

    LoginDetails getByCustomerID(long customerID);

    LoginDetails getByUsername(String username);

    void deleteByCustomerID(long customerID);

    default boolean isUserWithLogin(String login){
        AtomicBoolean is = new AtomicBoolean();
        findAll().stream()
                .filter(d-> d.getUsername().equals(login))
                .findFirst()
                .ifPresentOrElse(d->{
                    is.set(true);
                }, ()->{
                    is.set(false);
                });

        return is.get();
    }
}
