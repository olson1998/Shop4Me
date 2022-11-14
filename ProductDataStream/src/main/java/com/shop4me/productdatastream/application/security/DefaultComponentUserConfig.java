package com.shop4me.productdatastream.application.security;

import com.shop4me.productdatastream.domain.port.persisting.componentuser.ComponentUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j

@Configuration
@RequiredArgsConstructor
public class DefaultComponentUserConfig {

    private final ComponentUserRepository componentUserRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public void persistDefaultUserIfNotPresent(){
        persistDefaultUserIfNotPresent("Shop4Me_Core", "qwerty12345$", "Shop4Me_Core");
        persistDefaultUserIfNotPresent("Admin", "pass", "Shop4Me_Admin");
    }

    private void persistDefaultUserIfNotPresent(String username, String password, String authority){
        if(!componentUserRepository.existComponentUser(username, password)){
            var encodedPassword = passwordEncoder.encode(password);
            componentUserRepository.save(username, encodedPassword, authority);
        }
    }
}
