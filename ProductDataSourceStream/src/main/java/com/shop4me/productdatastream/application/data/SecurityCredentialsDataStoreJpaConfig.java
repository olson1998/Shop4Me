package com.shop4me.productdatastream.application.data;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.dialect.MariaDB103Dialect;
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
public class SecurityCredentialsDataStoreJpaConfig {


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

    @Bean
    public DataSource securityCredentialsDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(driver));
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        establishingConnectionLog(dataSource);
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean securityCredentialsEntitiesManager() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setPersistenceUnitName(persistenceUnitName);
        em.setDataSource(securityCredentialsDataSource());
        em.setPackagesToScan(entitiesPackage);
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(jpaProperties());
        return em;
    }

    @Bean
    public PlatformTransactionManager securityCredentialsTransactionManager() {
        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                securityCredentialsEntitiesManager().getObject()
        );
        return transactionManager;
    }

    private Properties jpaProperties(){
        Properties jpaProperties = new Properties();
        jpaProperties.put(DIALECT, MariaDB103Dialect.class.getName());
        jpaProperties.put(STORAGE_ENGINE, "InnoDB");
        jpaProperties.put(HBM2DDL_AUTO, "update");
        return jpaProperties;
    }

    private void establishingConnectionLog(DataSource dataSource) {
        try {
            String catalog = dataSource.getConnection().getCatalog();
            log.info("Connection={driver='{}', catalog='{}', user='{}'}",
                    driver,
                    catalog,
                    username
            );
        } catch (java.sql.SQLException e) {
            log.error("Connection failed={driver='{}', user='{}', reason='{}'}",
                    driver,
                    username,
                    e.toString()
            );
        }
    }

}
