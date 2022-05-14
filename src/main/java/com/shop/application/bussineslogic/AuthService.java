package com.shop.application.bussineslogic;

import com.shop.application.entities.roledb.Roles;
import com.shop.application.entities.userdb.LoginDetails;
import com.shop.application.repositories.rolesdbdao.RolesRepo;
import com.shop.application.repositories.userdbdao.LoginDetailsRepo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Log4j2
@AllArgsConstructor

@Service
public class AuthService implements UserDetailsService {

    private final BCryptPasswordEncoder encoder;
    private final LoginDetailsRepo repository;
    private final RolesRepo rolesRepo;

    public List<LoginDetails> all(){
        return repository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<LoginDetails> auth = repository.getByUsername(username);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        LoginDetails details = new LoginDetails(
                -1,
                "NOT_PROVIDED",
                encoder.encode("NOT_PROVIDED"),
                new ArrayList<>()
        );
        if(auth.isEmpty()){
            log.warn("User '"+ username +"' has not been found...");
        }
        else {
            details = auth.get();
            details.setRoles(rolesRepo.getAllRolesNamesByCustomerID(details.getCustomerID()));
            details.getRoles().forEach(role->{
                authorities.add(new SimpleGrantedAuthority(role));
            });
            log.info("User '" + details.getUsername() + "' has been found in db! Granted authorities: " + authorities);
        }
        return new org.springframework.security.core.userdetails.User(
                details.getUsername(),
                details.getPassword(),
                authorities
        );
    }

    public String saveNewUser(LoginDetails details){
        String response = "";
        boolean isunique = false;
        if(details.getUsername() == null){
            response ="Given username is empty!";
            log.error(response);
        }
        else if(details.getPassword()==null){
            response = "Given password is empty!";
            log.error(response);
        }
        else{
            isunique = repository.isUserWithLogin(details.getUsername());
        }
        if(isunique){
            details.setPassword(encoder.encode(details.getPassword()));
            details.setCustomerID(generateNewCustomerID());
            repository.save(details);
            rolesRepo.save(new Roles(
                    new ObjectId(),
                    details.getCustomerID(),
                    Collections.singletonList("LOGGED_USER")
            ));
            response = "Saved new user: " + details.getUsername();
            log.info(response);
        }
        else{
            response = "Couldn't save new user... " + details.toString();
            log.warn(response);
        }
        return response;
    }

    public void deleteUser(LoginDetails details){
        this.repository.deleteByCustomerID(details.getCustomerID());
    }

    private int generateNewCustomerID(){
        int id = repository.findLastID()+1;
        log.info("Generated new Customer id: " + id);
        return id;
    }
}
