package com.shop.application.config.multipledb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Log4j2

@EnableMongoRepositories(basePackages = "com.shop.application.repositories.rolesdbdao")

@Configuration
public class MongoClientConfig {

    @Bean
    public MongoClient mongo() {
        ConnectionString connectionString = new ConnectionString(
                //"mongodb://user-roles-mongodb.mongo.cosmos.azure.com:10255/user-roles-mongodb?ssl=true&replicaSet=globaldb&retrywrites=false&maxIdleTimeMS=120000"
                "mongodb://user-roles-mongodb:YKb2LMim5osizDoiV08u8CsrAKKN8KOTV7oozV7v0qoeVwFRrfjLGBF9fSvDdzvTg4CBda1Is1wC5CtHTRtA8A==@user-roles-mongodb.mongo.cosmos.azure.com:10255/?ssl=true&replicaSet=globaldb&retrywrites=false&maxIdleTimeMS=120000&appName=@user-roles-mongodb@"
        );
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongo(), "user-roles-mongodb");
    }
}
