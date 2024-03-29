package com.shop4me.productdatastream.domain.port.persisting.componentuser;

import com.shop4me.productdatastream.domain.port.objects.dto.ComponentUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ComponentUserRepository {

    @Transactional("securityCredentialsTransactionManager")
    Optional<ComponentUser> getComponentUserByUsername(String username);

    @Transactional("securityCredentialsTransactionManager")
    boolean existComponentUser(String username, String password);

    @Transactional("securityCredentialsTransactionManager")
    void save(String username, String password, String authority);
}
