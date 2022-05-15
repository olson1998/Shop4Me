package com.shop.application.entities.auctionsdb;

import lombok.*;

import javax.persistence.*;

@Data
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "customers_data")
public class Customer {

    @Id
    @Column(name = "customer_id")
    private int customerID;

    @Column(name="verified", nullable = false)
    private boolean isVerified;

    @Column(name = "email", nullable = false, length = 345, unique = true)
    private String email;

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

    @Column(name = "unit")
    private Integer unit;

    @Column(name="flat", length = 80)
    private Integer flat;

    @Column(name="city", length = 50)
    private String city;

    @Column(name="state", length = 50)
    private String state;

    @Column(name = "country", length = 50)
    private String country;

    @Column(name="zip", length = 50)
    private String ZIP;

    @Override
    public String toString() {
        return prefix + ". " + name + " " + surname + "\n" +
                phone + "\n" +
                building_name + "\n"
                + street_address + " " + unit + "/" +flat + "\n" +
                ZIP + " " + city + "\n" +
                state + "\n" +
                country
                ;
    }
}
