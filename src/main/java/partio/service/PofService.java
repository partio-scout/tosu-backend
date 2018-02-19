package partio.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
public class PofService extends RestTemplate {

    private URI uri;
    private final RestTemplate restTemplate;

    private LocalDate lastUpdate;
    private ObjectNode pofData;

    public PofService() {
        this.restTemplate = new RestTemplate();
        this.uri = URI.create("https://pof-backend.partio.fi/spn-ohjelma-json-taysi/?postGUID=86b5b30817ce3649e590c5059ec88921");
        lastUpdate = LocalDate.MIN;
        try {
            getPof();
        } catch (IOException ex) {
            System.err.println("initializing pof failed");
        }
    }

    @Transactional
    public ObjectNode getPof() throws IOException {
        if (lastUpdate != LocalDate.now()) {

            lastUpdate = LocalDate.now();
            pofData = fetchDataFromPof();
        }
        return pofData;

    }

    private ObjectNode fetchDataFromPof() throws IOException {
        String json = restTemplate.getForObject(uri, String.class);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, ObjectNode.class);
    }

}
