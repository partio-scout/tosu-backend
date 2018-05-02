
package partio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@ComponentScan()
public class WebConfig extends WebMvcConfigurerAdapter {
    
    @Autowired
    CounterInterceptor counterInterceptor;
	
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(counterInterceptor);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:3001",
                        "http://localhost:3000",
                        "https://localhost:3001",
                        "https://localhost:3000",
                        "https://suunnittelu.partio-ohjelma.fi",
                        "https://suunnittelu.partio-ohjelma.fi:3001",
                        "https://suunnittelu.partio-ohjelma.fi:3001")   
                .allowedHeaders("Authorization", "Content-Type", "Access-Control-Allow-Origin")
            //    .exposedHeaders("Authorization", "Content-Type", "Access-Control-Allow-Origin")
                .allowedMethods("PUT", "DELETE", "OPTIONS", "POST", "GET")
                .allowCredentials(true);
    }
    
}