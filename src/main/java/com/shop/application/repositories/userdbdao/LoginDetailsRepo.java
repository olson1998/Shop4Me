package com.shop.application.repositories.userdbdao;

import com.shop.application.entities.userdb.LoginDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public interface LoginDetailsRepo extends JpaRepository<LoginDetails, Long> {

    Optional<LoginDetails> getByUsername(String username);

    default Integer getCustomerIDbyUsername(String username){
        AtomicInteger id = new AtomicInteger();
        findAll().stream()
                .filter(user-> user.getUsername().equals(username))
                .map(LoginDetails::getCustomerID)
                .findFirst()
                .ifPresent(id::set);
        return id.get();
    }

    void deleteByCustomerID(long customerID);

    boolean existsLoginDetailsByUsername(String username);

    default boolean isUserWithLogin(String login){
        AtomicBoolean is = new AtomicBoolean(true);
        findAll().stream()
                .filter(d-> d.getUsername().equals(login))
                .findFirst()
                .ifPresent(s-> is.set(false));

        return is.get();
    }

    default int findLastID(){
        AtomicInteger last = new AtomicInteger(1);
        findAll().stream()
                .map(LoginDetails::getCustomerID)
                .max(Comparator.naturalOrder())
                .ifPresent(last::set);
        return last.get();
    }
}
