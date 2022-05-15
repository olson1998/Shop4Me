package com.shop.application.entities.roledb;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor

@Document("Roles")
public class Roles {

    @Id
    private ObjectId _id;

    @Field("customer_id")
    private int customerID;

    @Field("roles")
    private List<String> roles;
}
