package com.shop.application.entities.userdb;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @Column(name = "customer_id")
    private int customerID;

    @Column(name ="name", nullable = false)
    private String name;
}
