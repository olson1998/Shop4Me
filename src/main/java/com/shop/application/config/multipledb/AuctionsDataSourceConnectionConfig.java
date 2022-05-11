package com.shop.application.config.multipledb;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
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

@Log4j2

@AllArgsConstructor

@Configuration
@EnableJpaRepositories(
        basePackages = "com.shop.application.repositories.auctionsdbdao",
        entityManagerFactoryRef = "auctionsEntityManager",
        transactionManagerRef = "auctionsDataTransactionManager"
)
public class AuctionsDataSourceConnectionConfig {

    private final Environment env;

    @Bean
    public DataSource auctionsDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("spring.datasource.driver-class-name")));
        dataSource.setUrl(env.getProperty("spring.auctions.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.auctions.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.auctions.datasource.password"));
        log.info("Auction's data source url: " + dataSource.getUrl());
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean auctionsEntityManager() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(auctionsDataSource());
        em.setPackagesToScan(
                "com.shop.application.entities.auctionsdb"
        );
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
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

    @Bean
    public PlatformTransactionManager auctionsDataTransactionManager() {
        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                auctionsEntityManager().getObject());
        return transactionManager;
    }
}
