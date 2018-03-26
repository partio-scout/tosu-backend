package partio.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FillPofService {

    public static final String TARPPO = "tarppo", TARPPODEV = "tarppodev";

    @Autowired
    private PofService pofService;

    private RestTemplate restTemplate;
    private static Map<String, ExpirableObject> filledPofData;

    public FillPofService() {
        restTemplate = new RestTemplate();
        filledPofData = new HashMap<>();
        filledPofData.put(TARPPO, new ExpirableObject());
        filledPofData.put(TARPPODEV, new ExpirableObject());
    }

    public ObjectNode getAgeGroup(String agegroup) throws IOException {
        updateFilledPofIfNeeded(agegroup);
        return (ObjectNode) filledPofData.get(agegroup).getContent();
    }

    private void updateFilledPofIfNeeded(String agegroup) throws IOException {
        if (!filledPofData.containsKey(agegroup)) {
            throw new IllegalArgumentException("no agegroup found for:" + agegroup);
        }
        ExpirableObject inMemoryFilledPof = filledPofData.get(agegroup);
        if (!inMemoryFilledPof.getLastUpdated().equals(LocalDate.now())) {

            JsonNode filledAgeGroupNode = getTaskGroupsOfAge(agegroup, pofService.getPof());
            filledPofData.get(agegroup).setContent(filledAgeGroupNode);
        }

    }

    private JsonNode getTaskGroupsOfAge(String age, ObjectNode rawPof) {
        JsonNode cutByAge = rawPof.findValue("agegroups");
        switch (age) {
            case TARPPO:
                return fill(cutByAge.get(3), false);
            case TARPPODEV:
                return fill(cutByAge.get(3), true);
            default:
                throw new IllegalArgumentException("no agegroup found for:" + age);
        }
    }

    private JsonNode fill(JsonNode ageGroupNode, boolean dev) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode filledPof = mapper.createObjectNode();
        System.out.println("enter");

        List<JsonNode> tasklists = ageGroupNode.findValues("tasks");
        if (dev) {
            tasklists = tasklists.subList(0, 5);
        }
        ArrayNode taskGroups = filledPof.putArray("agegroup");
        taskGroups.addAll(tasklists);

        int i = 0;
        for (JsonNode tasks : tasklists) {

            for (JsonNode shallowDescription : tasks) {
                //extract data here
                ObjectNode detailedNode = mapper.createObjectNode();

                //details
                URI detailUrl = URI.create(shallowDescription.findValue("languages").findValue("details").asText());
                JsonNode detailedTask = restTemplate.getForObject(detailUrl, JsonNode.class);
                detailedNode.setAll((ObjectNode) detailedTask);

                //suggesions
                if (shallowDescription.findValue("suggestions_details").findValue("details") != null) {
                    URI suggetionUrlUrl = URI.create(shallowDescription.findValue("suggestions_details").findValue("details").asText());
                    JsonNode pofSuggestionJson = restTemplate.getForObject(suggetionUrlUrl, JsonNode.class);
                    detailedNode.set("suggestions_details", pofSuggestionJson);
                } else {
                    detailedNode.set("suggestions_details", null);
                }

                //replace old data with extracted data
                ObjectNode shallowNode = (ObjectNode) shallowDescription;
                shallowNode.removeAll();
                shallowNode.setAll((ObjectNode) detailedNode);

            }

            System.out.println(++i + "/" + tasklists.size() + " done");
        }
        System.out.println("exit");
        return filledPof;
    }
}
