package com.shop.application.entities.userdb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "login_details")
public class LoginDetails {

    @Id
    @Column(name = "customer_id")
    @JsonIgnore
    private int customerID;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "pass", nullable = false)
    private String password;
}
