package com.shop.application.bussineslogic;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
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
                encoder.encode("NOT_PROVIDED")
        );
        if(auth.isEmpty()){
            log.warn("User '"+ username +"' has not been found...");
        }
        else {
            details = auth.get();
            rolesRepo.getAllRolesNamesByCustomerID(details.getCustomerID()).forEach(role->
                    authorities.add(new SimpleGrantedAuthority(role))
            );
            log.info("User '" + details.getUsername() + "' has been found in db! Granted authorities: " + authorities);
        }
        return new org.springframework.security.core.userdetails.User(
                details.getUsername(),
                details.getPassword(),
                authorities
        );
    }

    public Integer saveNewUser(LoginDetails details){
        Integer generated_id = null;
        if(details.getUsername() == null){
            log.error("Given username is empty!");
        }
        else if(details.getPassword()==null){
            log.error("Given password is empty!");
        }
        if(!repository.existsLoginDetailsByUsername(details.getUsername())){
            generated_id  = generateNewCustomerID();
            details.setPassword(encoder.encode(details.getPassword()));
            details.setCustomerID(generated_id);
            repository.save(details);
            rolesRepo.save(new Roles(
                    new ObjectId(),
                    details.getCustomerID(),
                    Collections.singletonList("LOGGED_USER")
            ));
            log.info("Saved new user: '" + details.getUsername() +"', given id: '" + generated_id + "'");
        }
        else{
            log.warn("Couldn't save new user... " + details.toString());
        }
        return generated_id;
    }

    public String deleteUser(LoginDetails details){
        String response = "Couldn't delete the user: '"+details.getUsername()+
                "', with id: '" + details.getCustomerID() +"'";
        if(repository.existsLoginDetailsByUsername(details.getUsername())){
            this.repository.deleteByCustomerID(details.getCustomerID());
        }
        if(repository.existsLoginDetailsByUsername(details.getUsername())){
            response = "Deleted user: '"+details.getUsername()+
                    "', with id: '" + details.getCustomerID() +"'";
        }
        return response;
    }

    public Integer getCustomerIDByUsernameGotFromToken(String token){
        String username = getUsernameFromToken(token);
        Integer id = null;
        if(repository.existsLoginDetailsByUsername(username)){
            id = repository.getCustomerIDbyUsername(username);
            log.info("Found id: '" + id + "' for user: '" + username + "'");
        }
        else{
            log.error("user has not been found...");
        }
        return id;
    }

    private String getUsernameFromToken(String header){
        String token = header.substring("Auth ".length());
        Algorithm algorithm = Algorithm.HMAC256("auth".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();
        log.info("Read username: '" + username + "' from token");
        return username;
    }

    private int generateNewCustomerID(){
        int id = repository.findLastID()+1;
        log.info("Generated new Customer id: " + id);
        return id;
    }
}
