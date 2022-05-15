package com.shop.application.bussineslogic;

import com.shop.application.entities.auctionsdb.Customer;
import com.shop.application.repositories.auctionsdbdao.CustomersRepo;
import com.shop.application.repositories.userdbdao.LoginDetailsRepo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@AllArgsConstructor

@Service
public class CustomerService{

    private final CustomersRepo repository;
    private final LoginDetailsRepo loginDetailsRepo;

    public Optional<Customer> findCustomerByCustomerID(Integer customer_id){
        Optional<Customer> customer = Optional.empty();
        if(customer_id !=null){
            customer = repository.getCustomerByCustomerID(customer_id);
        }
        if(customer.isPresent()){
            log.info("Returned customer personal data for customer: '" +
                    customer.get().getCustomerID()
            );
        }
        else{
            log.info("No Customer found for customer with id: '" + customer_id + "'");
        }
        return customer;
    }

    public String saveNewCustomer(Customer customer, String username){
        String response = "";
        Integer id = loginDetailsRepo.getCustomerIDbyUsername(username);
        if(id !=null){
            customer.setCustomerID(id);
            repository.save(customer);
            response = "Saved new customer personal data for user: '" +username + "'";
        }
        else {
            response ="User was not saved because there is no user with username: '" + username + "'";
        }
        log.info(response);
        return response;
    }

    public String deleteCustomerPersonalData(Customer customer){
        String response = "";
        if(repository.existsCustomerByCustomerBody(customer)){
            repository.delete(customer);
            response = "deleted customer";
            log.info(response);
        }else{
            log.info("Customer with id has not been found in db!");
            response="wrong customer id";
        }
        return response;
    }

    private Integer parseIntFromPathVar(String int_path_var){
        Integer result = null;
        try{
            result = Integer.parseInt(int_path_var);
            log.info("Parsed: '" + result + "', from path var");
        }catch (Exception e){
            log.error("Couldn't parse int from path var, given: '" + int_path_var + "', Exception: " + e);
        }
        return result;
    }
}
