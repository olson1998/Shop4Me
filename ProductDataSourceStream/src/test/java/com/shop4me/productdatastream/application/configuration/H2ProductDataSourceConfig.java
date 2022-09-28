package com.shop4me.productdatastream.application.configuration;

import org.hibernate.dialect.H2Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

import static org.hibernate.cfg.AvailableSettings.DIALECT;
import static org.hibernate.cfg.AvailableSettings.HBM2DDL_AUTO;

@TestConfiguration
public class H2ProductDataSourceConfig {

    @Value("${spring.jpa.product-data-store.entities.package}")
    private String entitiesPackage;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Bean
    @Primary
    public DataSource productDbDataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:productdb;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1;DEFAULT_LOCK_TIMEOUT=10000;LOCK_MODE=0");
        dataSource.setUsername("sa");
        dataSource.setPassword("pass");
        log.info("Connection={'org.h2.Driver', catalog='test', user='sa'}");
        return dataSource;
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean productDbEntitiesManager() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setPersistenceUnitName("test-scope");
        em.setDataSource(productDbDataSource());
        em.setPackagesToScan(entitiesPackage);
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(jpaProperties());
        return em;
    }

    @Bean
    @Primary
    public PlatformTransactionManager productDbTransactionManager() {
        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                productDbEntitiesManager().getObject()
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
