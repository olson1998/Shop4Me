package com.shop.application.entities.userdb;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "login_details")
public class LoginDetails {

    @Id
    @Column(name = "customer_id")
    private int customerID;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "pass", nullable = false)
    private String password;

    @OneToMany(mappedBy = "customerID" , fetch = FetchType.EAGER)
    private List<Role> roles;
}
