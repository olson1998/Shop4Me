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
                DataSourceURL.roles_url
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
