package com.shop4me.productdatastream.application.configuration;

import org.hibernate.dialect.Dialect;
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

    @Value("${spring.jpa.components-security-credentials.persistence.unit-name}")
    private String persistenceUnitName;

    private final String url =
            "jdbc:h2:mem:authdb;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1;DEFAULT_LOCK_TIMEOUT=10000;LOCK_MODE=0";

    private final Class<? extends java.sql.Driver> driverClass = org.h2.Driver.class;

    private final Class<? extends Dialect> mySqlDialectClass = H2Dialect.class;

    @Bean
    public DataSource securityCredentialsDataSource() {
        var dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClass.getName());
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername("auth");
        dataSource.setPassword("pass");
        logTestConnection(dataSource);
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean securityCredentialsEntitiesManager() {
        var vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        var em = new LocalContainerEntityManagerFactoryBean();
        em.setPersistenceUnitName(persistenceUnitName+"-test");
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
        jpaProperties.put(DIALECT, H2Dialect.class.getName());
        jpaProperties.put(HBM2DDL_AUTO, "create");
        return jpaProperties;
    }

    private void logTestConnection(DataSource dataSource){
        try {
            var catalog = dataSource.getConnection().getCatalog();
            log.info("{}: MySql: [url: '{}', catalog: '{}']",
                    persistenceUnitName+"-test",
                    url,
                    catalog
            );
        } catch (java.sql.SQLException e) {
            log.error("{}: MySql: [url: '{}'] ERROR: {}",
                    persistenceUnitName+"-test",
                    mySqlDialectClass,
                    e.toString()
            );
        }
    }
}
