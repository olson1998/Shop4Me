package com.shop4me.productdatastream.application.persisting;

import com.shop4me.productdatastream.domain.model.data.entities.componentssecuritycredentials.Shop4MeComponentUser;
import com.shop4me.productdatastream.domain.port.persisting.repositories.componentuser.ComponentUserRepository;
import com.shop4me.productdatastream.domain.service.persisting.componentuser.Shop4MeComponentUserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaContext;

@Configuration
public class ComponentUserRepositoryConfig {

    @Bean
    public ComponentUserRepository componentUserRepository(@NotNull JpaContext jpaContext){
        var componentUserEntityManager = jpaContext
                .getEntityManagerByManagedType(Shop4MeComponentUser.class);

        return new Shop4MeComponentUserService(componentUserEntityManager);
    }
}
