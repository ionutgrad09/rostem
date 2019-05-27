package rostem.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableJpaRepositories({"rostem.repository"})
@EnableTransactionManagement
@EnableCaching
public class JpaConfiguration {
    // Should we use Mysql( == false) or Postgres (== true)
    private Boolean isProduction = false;

    @Value("${db.jdbcUrl}")
    private String jdbcUrl;

    @Value("${db.username}")
    private String username;

    @Value("${db.password}")
    private String password;

    @Value("${db.generateDDL}")
    private Boolean generateDDL;

    @Value("${db.driver_class}")
    private String driverClass;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();

        if(isProduction) {

            String herokuUrl = System.getenv("JDBC_DATABASE_URL");
            String herokuUsername = System.getenv("JDBC_DATABASE_USERNAME");
            String herokuPassword = System.getenv("JDBC_DATABASE_PASSWORD");

            config.setJdbcUrl(herokuUrl);
            config.setUsername(herokuUsername);
            config.setPassword(herokuPassword);
        }
        else {
            config.setJdbcUrl(jdbcUrl);
            config.setUsername(username);
            config.setPassword(password);
            //config.setDriverClassName(driverClass);
        }
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        HikariDataSource dataSource = new HikariDataSource(config);
        return dataSource;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

//        // Checks if we're running on Heroku so that we can change the db to Postgres
//        if(isProduction == null) {
//            isProduction = System.getenv("IS_PRODUCTION") != null
//                    && System.getenv("IS_PRODUCTION").equals("TRUE");
//        }
//
//        if(isProduction) {
//            vendorAdapter.setDatabase(Database.POSTGRESQL);
//        }
//        else{
//            vendorAdapter.setDatabase(Database.MYSQL);
//        }

        vendorAdapter.setDatabase(Database.POSTGRESQL);
        vendorAdapter.setGenerateDdl(generateDDL);
        vendorAdapter.setShowSql(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("rostem.model");
        factory.setDataSource(dataSource());
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean
    public EntityManager entityManager() {
        return entityManagerFactory().createEntityManager();
    }

    @Bean
    PlatformTransactionManager transactionManager() {
        JpaTransactionManager manager = new JpaTransactionManager();
        manager.setEntityManagerFactory(entityManagerFactory());
        return manager;
    }

    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator() {
        return new HibernateExceptionTranslator();
    }

}
