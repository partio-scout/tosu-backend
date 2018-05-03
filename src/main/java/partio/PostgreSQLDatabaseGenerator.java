
package partio;

import java.util.HashMap;
import java.util.Map;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import partio.domain.*;

// aws had trouble setting database automatically, this fixed it
public class PostgreSQLDatabaseGenerator {
    
    public void generate() {
        Map<String, String> settings = new HashMap<>();
        settings.put("spring.datasource.driverClassName", "org.postgresql.Driver");
        settings.put("spring.jpa.database-platform", "org.hibernate.dialect.PostgreSQLDialect");
        settings.put("hibernate.connection.url", "jdbc:postgresql://localhost:5432/tosudb?useSSL=false");
        settings.put("hibernate.connection.username", "postgres");
        settings.put("hibernate.connection.password", "tosudb");
        settings.put("hibernate.hbm2ddl.auto", "create");
        settings.put("show_sql", "true");
        settings.put("server.port", "3001");
        settings.put("server.ssl.key-store-type", "PKCS12");
        settings.put("server.ssl.key-store", "certificate.p12");
        settings.put("server.ssl.key-store-password", "tosudb");

        MetadataSources metadata = new MetadataSources(
                new StandardServiceRegistryBuilder()
                        .applySettings(settings)
                        .build());
        metadata.addAnnotatedClass(Event.class);
        metadata.addAnnotatedClass(EventGroup.class);
        metadata.addAnnotatedClass(Activity.class);
        metadata.addAnnotatedClass(ActivityBuffer.class);
        metadata.addAnnotatedClass(Scout.class);
        SchemaExport schemaExport = new SchemaExport(
                (MetadataImplementor) metadata.buildMetadata()
        );
        schemaExport.setHaltOnError(true);
        schemaExport.setFormat(true);
        schemaExport.setDelimiter(";");
        schemaExport.setOutputFile("db-schema.sql");
        schemaExport.execute(true, true, false, true);
    }
    
}
