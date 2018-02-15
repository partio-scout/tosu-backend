package partio.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.net.URI;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public final class PofService extends RestTemplate {

    private URI uri;
    private final RestTemplate restTemplate;

    public PofService() {
        this.restTemplate = new RestTemplate();
        setUri("https://pof-backend.partio.fi/spn-ohjelma-json-taysi/?postGUID=86b5b30817ce3649e590c5059ec88921");
    }

    public void setUri(String uri) {
        this.uri = URI.create(uri);
    }

    public Object getPof() throws IOException {
        String json = restTemplate.getForObject(uri, String.class);
        ObjectMapper mapper = new ObjectMapper();
        Object jsonObject = mapper.readValue(json, ObjectNode.class);
        return jsonObject;
    }

}
