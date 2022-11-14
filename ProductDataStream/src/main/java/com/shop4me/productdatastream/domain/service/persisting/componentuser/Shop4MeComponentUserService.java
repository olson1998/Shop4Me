package com.shop4me.productdatastream.domain.service.persisting.componentuser;

import com.shop4me.productdatastream.domain.model.dao.componentssecuritycredentials.Shop4MeComponentUser;
import com.shop4me.productdatastream.domain.port.persisting.componentuser.ComponentUserRepository;
import com.shop4me.productdatastream.domain.port.objects.dto.ComponentUser;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.Optional;

@RequiredArgsConstructor
public class Shop4MeComponentUserService implements ComponentUserRepository {

    private final EntityManager componentUserEntityManager;

    @Override
    public Optional<ComponentUser> getComponentUserByUsername(String username) {
        return Optional.of(selectComponentUserByUsername(username));
    }

    @Override
    public boolean existComponentUser(String username, String password) {
        return componentUserEntityManager
                .createQuery(
                        "select case when count(u.username)>0 " +
                        "then true else false end from Shop4MeComponentUser u " +
                        "where u.username=:username and u.password=:pass",
                        Boolean.class)
                .setParameter("username", username)
                .setParameter("pass", password)
                .getSingleResult();
    }

    @Override
    public void save(String username, String password, String authority) {
        var user = new Shop4MeComponentUser(username, password, authority);
        componentUserEntityManager.persist(user);
    }

    public Shop4MeComponentUser selectComponentUserByUsername(String username) {
        return componentUserEntityManager
                .createQuery(
                        "select u from Shop4MeComponentUser u where u.username= :username",
                        Shop4MeComponentUser.class)
                .setParameter("username", username)
                .getSingleResult();
    }
}
