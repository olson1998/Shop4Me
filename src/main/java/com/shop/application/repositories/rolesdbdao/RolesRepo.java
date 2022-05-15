package com.shop.application.repositories.rolesdbdao;

import com.shop.application.entities.roledb.Roles;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public interface RolesRepo extends MongoRepository<Roles, Double> {

    Optional<Roles> findByCustomerID(int customer_id);

    default List<String> getAllRolesNamesByCustomerID(int customer_id){
        List<String> names = new ArrayList<>();
        Optional<Roles> roles = findByCustomerID(customer_id);
        AtomicBoolean is = new AtomicBoolean(false);
        roles.ifPresent(r-> is.set(true));
        if(is.get()){
            names = roles
                    .get()
                    .getRoles();
        }
        return names;
    }
}