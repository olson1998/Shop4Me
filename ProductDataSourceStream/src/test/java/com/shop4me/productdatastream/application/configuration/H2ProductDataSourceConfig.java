package com.shop4me.productdatastream.application.configuration;

import org.hibernate.dialect.Dialect;
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

    @Value("${spring.jpa.product-data-store.persistence.unit-name}")
    private String persistenceUnitName;

    private final String url =
            "jdbc:h2:mem:productdb;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1;DEFAULT_LOCK_TIMEOUT=10000;LOCK_MODE=0";

    private final Class<? extends java.sql.Driver> driverClass = org.h2.Driver.class;

    private final Class<? extends Dialect> mySqlDialectClass = H2Dialect.class;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Bean
    @Primary
    public DataSource productDataStreamDataSource(){
        var dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClass.getName());
        dataSource.setUrl(url);
        dataSource.setUsername("sa");
        dataSource.setPassword("pass");
        logTestConnection(dataSource);
        return dataSource;
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean productDataStreamEntitiesManager() {
        var vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        var em = new LocalContainerEntityManagerFactoryBean();
        em.setPersistenceUnitName(persistenceUnitName + "-test");
        em.setDataSource(productDataStreamDataSource());
        em.setPackagesToScan(entitiesPackage);
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(jpaProperties());
        return em;
    }

    @Bean
    @Primary
    public PlatformTransactionManager productDataStreamTransactionManager() {
        var transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                productDataStreamEntitiesManager().getObject()
        );
        return transactionManager;
    }

    private Properties jpaProperties(){
        var jpaProperties = new Properties();
        jpaProperties.put(DIALECT, mySqlDialectClass);
        jpaProperties.put(HBM2DDL_AUTO,  "create");
        return jpaProperties;
    }

    private void logTestConnection(DataSource dataSource){
        try {
            var catalog = dataSource.getConnection().getCatalog();
            log.info("{}: MySql: [url: '{}', catalog: '{}', driver: '{}']",
                    persistenceUnitName+"-test",
                    url,
                    catalog,
                    driverClass
            );
        } catch (java.sql.SQLException e) {
            log.error("{}: MySql: [url: '{}', driver: '{}'] ERROR: {}",
                    persistenceUnitName+"-test",
                    mySqlDialectClass,
                    driverClass,
                    e.toString()
            );
        }
    }
}
