package com.shop.application.entities;

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
    private long customerID;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "customerID" , fetch = FetchType.EAGER)
    private List<Role> roles;
}
