package partio;

import java.io.IOException;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import partio.service.PofService;

@Configuration
@EnableWebMvc
@EnableScheduling
public class WebConfig extends WebMvcConfigurerAdapter {

    private PofService pof;

    public void addScheduler() {
        pof = new PofService();
        try {
            pof.testScheduler();
        } catch (IOException ex) {
            System.out.print(ex);
        }
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:3000",
                        "https://suunnittelu.partio-ohjelma.fi")
                .allowedMethods("GET")
                .allowedHeaders("Access-Control-Allow-Origin")
              //  .exposedHeaders("Access-Control-Allow-Origin")
                .allowCredentials(true);
    }
}
