package partio.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class FilledPof {

    public static final String TARPPO = "tarppo", TARPPODEV = "tarppodev";

    private final RestTemplate restTemplate;
    private static Map<String, ObjectNode> filledPofData;

    //here is held pofdata filled with other details that front end needs
    //which are found behind description link in rawpof
    public FilledPof() {
        restTemplate = new RestTemplate();
        filledPofData = new HashMap<>();
        filledPofData.put(TARPPO, null);
        filledPofData.put(TARPPODEV, null);
    }

    //return full info of an agegroup
    public ObjectNode getAgeGroup(String agegroup, ObjectNode rawPofData) throws IOException, NullPointerException {
        if (!filledPofData.containsKey(agegroup)) {
            throw new IllegalArgumentException("no agegroup found for:" + agegroup);
        }
        return filledPofData.get(agegroup);
    }

    //update an agegroup
    public void updateFilledPof(String agegroup, ObjectNode rawPofData) throws IOException {
        if (!filledPofData.containsKey(agegroup)) {
            throw new IllegalArgumentException("no agegroup found for:" + agegroup +" in map of agegroups");
        }
        JsonNode filledAgeGroupNode = getTaskGroupsOfAgeFromRawPof(agegroup, rawPofData);
        filledPofData.put(agegroup, (ObjectNode) filledAgeGroupNode);

    }

    //here we select which index is chosen. 
    //May choose dev for less info to retrieved with dev for inspecting
    //dev break front end since the rest arent handled. Use to read json structure
    private JsonNode getTaskGroupsOfAgeFromRawPof(String age, ObjectNode rawPof) {
        JsonNode cutByAge = rawPof.findValue("agegroups");
        switch (age) {
            case TARPPO:
                return fillTasks(cutByAge.get(3), false);
            case TARPPODEV:
                return fillTasks(cutByAge.get(3), true);
            default:
                throw new IllegalArgumentException("no agegroup found for:" + age);
        }
    }

    //here we simply follow the link and replace the original tasks
    //async operation would make this faster
    private JsonNode fillTasks(JsonNode ageGroupNode, boolean dev) {
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("enter");

        List<JsonNode> tasklists = ageGroupNode.findValues("tasks");
        if (dev) {
            tasklists = tasklists.subList(0, 5);
        }

        int i = 0;
        for (JsonNode tasks : tasklists) {

            for (JsonNode shallowDescription : tasks) {
                //extract data here
                ObjectNode detailedNode = mapper.createObjectNode();

                //details
                URI detailUrl = URI.create(shallowDescription.findValue("languages").findValue("details").asText());
                JsonNode detailedTask = restTemplate.getForObject(detailUrl, JsonNode.class);
                detailedNode.setAll((ObjectNode) detailedTask);

                //suggestions
                System.out.println("start descr");
                if (shallowDescription.findValue("suggestions_details").findValue("details") != null) {
                    System.out.println("sugg yeeeee");
                    URI suggetionUrlUrl = URI.create(shallowDescription.findValue("suggestions_details").findValue("details").asText());
                    JsonNode pofSuggestionJson = restTemplate.getForObject(suggetionUrlUrl, JsonNode.class);
                     System.out.println(pofSuggestionJson.asText());
                      System.out.println(pofSuggestionJson);
                    detailedNode.set("suggestions_details", pofSuggestionJson);
                } else {
                    System.out.println("sugg is not found");
                    detailedNode.set("suggestions_details", null);
                }
               
                //add key and value that treesearch uses
                //replace old data with extracted data
                ObjectNode shallowNode = (ObjectNode) shallowDescription;
                shallowNode.removeAll();
                shallowNode.setAll((ObjectNode) detailedNode);

            }
//loggin how far done
            System.out.println(++i + "/" + tasklists.size() + " done");
        }
        System.out.println("exit");
        return ageGroupNode;
    }
}
