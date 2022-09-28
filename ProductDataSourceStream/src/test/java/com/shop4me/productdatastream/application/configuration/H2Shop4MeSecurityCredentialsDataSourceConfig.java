package com.shop4me.productdatastream.application.configuration;

import org.hibernate.dialect.H2Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

import static org.hibernate.cfg.AvailableSettings.DIALECT;
import static org.hibernate.cfg.AvailableSettings.HBM2DDL_AUTO;

@TestConfiguration
public class H2Shop4MeSecurityCredentialsDataSourceConfig {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    @Value("${spring.jpa.components-security-credentials.package}")
    private String entitiesPackage;

    @Bean
    public DataSource securityCredentialsDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(driver));
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:authdb;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1;DEFAULT_LOCK_TIMEOUT=10000;LOCK_MODE=0");
        dataSource.setUsername("auth");
        dataSource.setPassword("pass");
        log.info("Connection={'org.h2.Driver', catalog='authdb', user='auth'}");
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean securityCredentialsEntitiesManager() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setPersistenceUnitName("shop4me-sc-test");
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
        jpaProperties.put(DIALECT, H2Dialect.class.getName());
        jpaProperties.put(HBM2DDL_AUTO, "create");
        return jpaProperties;
    }

}
