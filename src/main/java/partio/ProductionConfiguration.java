 package partio;

import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("production")
@Configuration
public class ProductionConfiguration {
    
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        PostgreSQLDatabaseGenerator postgreSQLDatabaseGenerator = new PostgreSQLDatabaseGenerator();
        postgreSQLDatabaseGenerator.generate();
        return DataSourceBuilder.create().build();
    }
}
