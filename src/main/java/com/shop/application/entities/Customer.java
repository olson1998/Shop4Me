package com.shop.application.entities;

import lombok.*;

import javax.persistence.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "customers_data")
public class Customer {

    @Id
    @Column(name = "customer_id")
    private long customer_id;

    @Column(name="verified", nullable = false)
    private boolean isVerified;

    @Column(name = "email", nullable = false, length = 345, unique = true)
    private String email;

    @Column(name = "username", nullable = false, length = 30)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "prefix", length = 10)
    private String prefix;

    @Column(name = "first_name", length = 30)
    private String name;

    @Column(name="surname", length = 50)
    private String surname;

    @Column(name="building_name", length = 50)
    private String building_name;

    @Column(name = "street_address", length = 100)
    private String street_address;

    @Column(name = "unit", columnDefinition = "INT(11) UNSIGNED")
    private Integer unit;

    @Column(name="flat", columnDefinition= "INT(11) UNSIGNED")
    private Integer flat;

    @Column(name="city", length = 50)
    private String city;

    @Column(name="state", length = 50)
    private String state;

    @Column(name="zip", length = 50)
    private String ZIP;
}
