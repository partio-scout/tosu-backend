package partio.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
public class PofService extends RestTemplate {

    private final RestTemplate restTemplate;
    private ExpirableObject<ObjectNode> pof;
    private FillPofService filledPof;

    public PofService() {
        pof = new ExpirableObject<>();
        this.restTemplate = new RestTemplate();
        filledPof = new FillPofService();
    }

    //rawpof
    public ObjectNode getPof() throws IOException {
        updatePofIfNeeded();
        return pof.getContent();
    }

    private void updatePofIfNeeded() throws IOException {
        if (!pof.getLastUpdated().equals(LocalDate.now())) {
            pof.setContent(fetchDataFromPof());
        }
    }

    private ObjectNode fetchDataFromPof() throws IOException {
        URI uri = URI.create("https://pof-backend.partio.fi/spn-ohjelma-json-taysi/?postGUID=86b5b30817ce3649e590c5059ec88921");
        String json = restTemplate.getForObject(uri, String.class);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, ObjectNode.class);
    }

//    @Scheduled(fixedRate=5000)
    @Scheduled(cron = "0 0 2 * * *", zone = "Europe/Athens")
    public void testScheduler() throws IOException {
        reload();
    }

    public void reload() throws IOException {
        ObjectNode loaded = fetchDataFromPof();
        pof.setContent(loaded);
        filledPof.reload();
    }
}
