package com.shop4me.productdatastream.domain.port.persisting.repositories.componentuser;

import com.shop4me.productdatastream.domain.port.persisting.dto.entity.ComponentUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ComponentUserRepository {

    @Transactional("securityCredentialsTransactionManager")
    Optional<ComponentUser> getComponentUserByUsername(String username);

}
