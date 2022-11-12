package com.shop4me.productdatastream.application.security;

import com.shop4me.productdatastream.domain.port.persisting.componentuser.ComponentUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j

@RequiredArgsConstructor
public class Shop4MeProductDataStreamAuthService implements UserDetailsService {

    private final ComponentUserRepository componentUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userDetailsRef = new AtomicReference<UserDetails>();

        componentUserRepository.getComponentUserByUsername(username)
                .ifPresentOrElse(componentUser ->{
                    log.info("Attempt to authenticate: {}", componentUser);
                    var userDetails = new User(
                            componentUser.getUsername(),
                            componentUser.getPassword(),
                            Set.of(new SimpleGrantedAuthority(componentUser.getAuthority()))
                    );
                    userDetailsRef.set(userDetails);
                }, ()->{
                    log.info("User with username: '{}' has not been found...", username);
                    throw new UsernameNotFoundException(
                            "User with username: +"+ username +" has not been found..."
                    );
                });
        return userDetailsRef.get();
    }

}
