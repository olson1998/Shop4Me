package com.shop4me.productdatastream.domain.model.dao.componentssecuritycredentials;

import com.shop4me.productdatastream.domain.port.objects.dto.ComponentUser;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Getter

@NoArgsConstructor

@Entity
@Table(name = "components_login_credentials")
public class Shop4MeComponentUser implements ComponentUser {

    @Id
    @Column(name = "user_id", length = 40, updatable = false)
    private final String id = UUID.randomUUID().toString();

    @Column(name = "username", length = 40, nullable = false, unique = true, updatable = false)
    private String username;

    @Column(name = "password", length = 200, nullable = false)
    private String password;
    
    @Column(name = "authority", length = 100, nullable = false)
    private String authority;

    @Override
    public String toString() {
        return "Shop4MeComponentUser{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", authority='" + authority + '\'' +
                '}';
    }

    public Shop4MeComponentUser(String username, String password, String authority) {
        this.username = username;
        this.password = password;
        this.authority = authority;
    }
}
