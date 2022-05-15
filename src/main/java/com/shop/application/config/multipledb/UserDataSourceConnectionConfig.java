package com.shop.application.config.multipledb;

import lombok.AllArgsConstructor;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

@AllArgsConstructor

@Configuration
@EnableJpaRepositories(
        basePackages = "com.shop.application.repositories.userdbdao",
        entityManagerFactoryRef = "userEntityManager",
        transactionManagerRef = "privateUserDataTransactionManager"
)
public class UserDataSourceConnectionConfig {

    private final Environment env;

    @Primary
    @Bean
    public DataSource userDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("spring.datasource.driver-class-name")));
        dataSource.setUrl(env.getProperty("spring.user.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.user.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.user.datasource.password"));
        return dataSource;
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean userEntityManager() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(userDataSource());
        em.setPackagesToScan(
                "com.shop.application.entities.userdb"
        );
        HibernateJpaVendorAdapter vendorAdapter
                = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        Properties properties = new Properties();
        properties.put(
                "spring.jpa.hibernate.ddl-auto",
               env.getProperty("spring.jpa.hibernate.ddl-auto")
        );
        properties.put(
                "spring.jpa.generate-ddl",
                env.getProperty("spring.jpa.generate-ddl")
        );
        em.setJpaProperties(properties);
        return em;
    }

    @Primary
    @Bean
    public PlatformTransactionManager privateUserDataTransactionManager() {
        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                userEntityManager().getObject());
        return transactionManager;
    }
}
