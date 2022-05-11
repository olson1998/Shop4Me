package com.shop.application.entities.roledb;

import lombok.*;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Roles {

    @Id
    private int customer_id;

    private List<String> names;
}
