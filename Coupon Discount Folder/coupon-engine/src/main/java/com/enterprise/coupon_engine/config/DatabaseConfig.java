package com.enterprise.coupon_engine.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement // Explicitly enables processing of declarative @Transactional boundaries
public class DatabaseConfig {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    /**
     * Programmatic configuration of the HikariCP connection pool.
     * Hardens the database layer against connection leakage during high concurrent locking phases.
     */
    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        
        config.setJdbcUrl(dbUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driverClassName);

        // Advanced Tuning Parameters for High Concurrency Performance
        config.setMaximumPoolSize(50);
        config.setMinimumIdle(10);
        config.setIdleTimeout(10000); // 10 seconds idle lifespan before cleanup
        config.setConnectionTimeout(30000); // 30 seconds maximum wait time for an available thread allocation
        config.setMaxLifetime(1800000); // 30 minutes absolute connection lifespan retirement
        config.setPoolName("CouponEngineHikariCP");

        return new HikariDataSource(config);
    }

    /**
     * Explicit transaction manager declaration to govern database connection lifecycle scopes.
     */
    @Bean
    public PlatformTransactionManager transactionManager(@NonNull EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
