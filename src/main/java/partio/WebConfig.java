package partio;

/*
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
*/
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://localhost:3000", "http://localhost:3000")
                .allowedMethods("PUT", "DELETE", "POST", "GET");
        
        //jos haluaa tarkentaa näitä myöhemmin
        //		.allowedHeaders("header1", "header2", "header3")
        //		.exposedHeaders("header1", "header2")
        //		.allowCredentials(false).maxAge(3600); //cache jtn
    }

    //KARI KATO NAA ETTEI MEE SUN KONFFAUKSIEN KAA SEKAISIN
    //corssi haluaa että määritellään onko http vai https
    //->ennen pushia masteriin joutuis vaihtaa kaikki frontin http ->https
    //tän takia tein nämäkin tähän näi kun google näytti nää samalla
    
    //laiton keytoolin ssl sisään resourcekansioon ja
    //jos hyväksyt nää ota kommentit pois application.properties
    //tein tän ssl:n frontti herjas ja tämä ehkä auttaa devaukses
    
    

    //bonuksena jos http pyynto tulee se redirectaa sen
    //en ole varma toimiiko frontin mukana kivasti
    //huono puoli localhost aina haluaa portin localhost/event ei toimi
    //redirectaa 8080->3001 ihan hyvin kuitenkin
    
    /*
    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };

      //  tomcat.addAdditionalTomcatConnectors(initiateHttpConnector());
        return tomcat;
    }    
    //new connctor redirect http reqs to https
    private Connector initiateHttpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(8080);
        connector.setSecure(false);
        connector.setRedirectPort(3001);

        return connector;
    }*/
}
