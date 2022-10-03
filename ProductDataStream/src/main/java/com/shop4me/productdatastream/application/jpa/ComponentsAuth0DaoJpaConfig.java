package com.shop4me.productdatastream.application.jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

import static org.hibernate.cfg.AvailableSettings.*;

@Slf4j

@Configuration
public class ComponentsAuth0DaoJpaConfig {


    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    @Value("${spring.jpa.components-security-credentials.package}")
    private String entitiesPackage;

    @Value("${spring.jpa.components-security-credentials.persistence.unit-name}")
    private String persistenceUnitName;

    @Value("${spring.datasource.components-security-credentials.base.url}")
    private String url;

    @Value("${spring.datasource.components-security-credentials.username}")
    private String username;

    @Value("${spring.datasource.components-security-credentials.password}")
    private String password;

    @Value("${spring.datasource.components-security-credentials.dialect}")
    private String mySqlDialect;

    @Value("${spring.datasource.components-security-credentials.storage-engine}")
    private String storageEngine;

    @Value("${spring.datasource.components-security-credentials.hbm2ddl}")
    private String hbm2ddl;

    @Bean
    public DataSource securityCredentialsDataSource() {
        var dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(driver));
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        establishingConnectionLog(dataSource);
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean securityCredentialsEntitiesManager() {
        var vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        var em = new LocalContainerEntityManagerFactoryBean();
        em.setPersistenceUnitName(persistenceUnitName);
        em.setDataSource(securityCredentialsDataSource());
        em.setPackagesToScan(entitiesPackage);
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(jpaProperties());
        return em;
    }

    @Bean
    public PlatformTransactionManager securityCredentialsTransactionManager() {
        var transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                securityCredentialsEntitiesManager().getObject()
        );
        return transactionManager;
    }

    private Properties jpaProperties(){
        var jpaProperties = new Properties();
        jpaProperties.put(DIALECT, mySqlDialect);
        jpaProperties.put(STORAGE_ENGINE, storageEngine);
        jpaProperties.put(HBM2DDL_AUTO, hbm2ddl);
        return jpaProperties;
    }

    private void establishingConnectionLog(DataSource dataSource) {
        try {
            var catalog = dataSource.getConnection().getCatalog();
            log.debug("{}: MySql: [url: '{}', user: '{}', password: {} characters, catalog: '{}', storage engine: '{}']",
                    persistenceUnitName,
                    url,
                    username,
                    password.length(),
                    catalog,
                    storageEngine
            );
        } catch (java.sql.SQLException e) {
            log.error("{}: MySql: [url: '{}', user: '{}', password: {} characters, storage engine: '{}'] ERROR: {}",
                    persistenceUnitName,
                    driver,
                    username,
                    password,
                    storageEngine,
                    e.toString()
            );
        }
    }

}
