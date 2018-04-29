package partio.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
public class PofService extends RestTemplate {

    private final RestTemplate restTemplate;
    private ObjectNode rawPof;
    @Autowired
    private FilledPof filledPof;

    //holds rawPof and commands filledpof to update
    public PofService() {
        this.restTemplate = new RestTemplate();
    }

    //    @Scheduled(fixedRate=5000) updates periodically
    @Scheduled(cron = "0 0 2 * * *", zone = "Europe/Athens")
    public void testScheduler() throws IOException {
        updatePofData(false);
    }

    public ObjectNode getPof() throws IOException {
        if (rawPof == null) { //means no data ->get it
            updatePofData(true);
        }
        return rawPof;
    }

    public FilledPof getFilledPof() throws IOException {
        if (rawPof == null) { //means no data ->get it
            updatePofData(true);
        }
        return filledPof;
    }

    private ObjectNode getPofFromWeb() throws IOException {
        URI uri = URI.create("https://pof-backend.partio.fi/spn-ohjelma-json-taysi/?postGUID=86b5b30817ce3649e590c5059ec88921");
        String json = restTemplate.getForObject(uri, String.class);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, ObjectNode.class);
    }

    //forcedupdate in case this silently will fail, it can be set to true to reload
    public void updatePofData(boolean forcedUpdate) throws IOException {
        ObjectNode freshlyLoadedPof = getPofFromWeb();

        LocalDateTime lastUpdateTime = PofUpdateDateConverter.getDateFromPof(rawPof);
        LocalDateTime freshPofUpdateTime = PofUpdateDateConverter.getDateFromPof(freshlyLoadedPof);
        
        if (freshPofUpdateTime.isAfter(lastUpdateTime) || forcedUpdate) {
            rawPof = freshlyLoadedPof;
            filledPof.updateFilledPof(FilledPof.TARPPO, freshlyLoadedPof);
            filledPof.updateFilledPof(FilledPof.TARPPODEV, freshlyLoadedPof);
        }

    }

}
