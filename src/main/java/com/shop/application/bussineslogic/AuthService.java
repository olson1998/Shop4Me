package com.shop.application.bussineslogic;

import com.shop.application.entities.LoginDetails;
import com.shop.application.repositories.LoginDetailsRepo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@AllArgsConstructor

@Service
public class AuthService implements UserDetailsService {

    private final BCryptPasswordEncoder encoder;
    private final LoginDetailsRepo repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginDetails details = repository.getByUsername(username);
        if(details == null){
            log.error("User '"+ username +"' has not been found...");
            throw new UsernameNotFoundException("user has not been found in DB");
        }
        else{
            log.info("User '" + username +"' has been found in DB!");
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        details.getRoles().forEach(role->{
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(
                details.getUsername(),
                encoder.encode(details.getPassword()),
                authorities
        );
    }

    public boolean saveNewUser(LoginDetails details){
        boolean isunique = repository.isUserWithLogin(details.getUsername());
        if(isunique){
            repository.save(details);
        }
        return isunique;
    }

    public void deleteUser(LoginDetails details){
        this.repository.deleteByCustomerID(details.getCustomerID());
    }
}
