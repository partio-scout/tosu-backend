package partio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import partio.controller.OptionsController;

@SpringBootApplication
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
        //PostgreSQLDatabaseGenerator postgreSQLDatabaseGenerator = new PostgreSQLDatabaseGenerator();
        //postgreSQLDatabaseGenerator.generate();
        SpringApplication.run(Application.class, args);
    }

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DispatcherServletInitializer.class, OptionsController.class,Application.class);
    }
}
