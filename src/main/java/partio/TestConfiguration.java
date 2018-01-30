package partio;

import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class TestConfiguration {
    /*
    Add here if some sort of test data is needed for exploratory testing.
    This should not reach production. HOWEVER this needs to be confirmed.
    
    Dev profile cannot exist separately. It has to come from spring itself.
    Otherwise you will have to manually tell database address for example.
    */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }
}
