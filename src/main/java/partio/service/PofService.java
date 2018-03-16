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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
public class PofService extends RestTemplate {

    private final String POF = "rawpofData", TARPPO = "tarppo", TARPPODEV = "tarppodev";
    private RestTemplate restTemplate;
    private Map<String, ExpirableObject> pofData;

    //   all/old pof
    public PofService() {
        pofData = new HashMap<>();
        pofData.put(TARPPO, new ExpirableObject());
        pofData.put(TARPPODEV, new ExpirableObject());
        pofData.put(POF, new ExpirableObject());

        this.restTemplate = new RestTemplate();
        try {
            getPof();
            updateageGroupTasksIfNeeded("tarppodev");
        } catch (IOException ex) {
            System.err.println("initializing pofservice failed");
        }
    }

    //rawpof
    public ObjectNode getPof() throws IOException {
        updatePofIfNeeded();
        return (ObjectNode) pofData.get(POF).getContent();
    }

    private void updatePofIfNeeded() throws IOException {
        ExpirableObject pof = pofData.get(POF);
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

    //new stuff
    public ResponseEntity<Object> getTasks(String ageGroup) throws IOException {
        if (pofData.containsKey(ageGroup)) {
            updateageGroupTasksIfNeeded(ageGroup);
            return ResponseEntity.ok(pofData.get(ageGroup).getContent());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    private void updateageGroupTasksIfNeeded(String ageGroup) throws IOException {
        //uses pof so it has to be updated as well
        updatePofIfNeeded();
        ExpirableObject tasks = pofData.get(ageGroup);
        if (!tasks.getLastUpdated().equals(LocalDate.now())) {
            tasks.setContent(getPofActivitiesOfAge(ageGroup));
        }
    }

    //new pof by agegroup
    private ArrayNode getPofActivitiesOfAge(String age) throws IOException {
        //get the agegroup of choice ONLY tarppo and tarppodev WORKS right now
        JsonNode pofOfAge = getTaskGroupsOfAge(age);
        //below returns tasks separated by taskgroups, so it's an array that has only tasks in them
        List<JsonNode> taskGroups = pofOfAge.findValues("tasks");
        //lets cut size if age contains dev in it
        if (age.contains("dev")) {
            int newSize = taskGroups.size() / 10;
            taskGroups = taskGroups.subList(taskGroups.size()-newSize, taskGroups.size()-1);
        }
        //here goes into forloop and visits every url of each task
        //to create our custom activity for frontend easy to read
        ArrayNode customTasks = getTasksFromTaskGroups(taskGroups);
        return customTasks;
    }

    //only tarppo/tarppodev for now
    private JsonNode getTaskGroupsOfAge(String age) {
        JsonNode cutByAge = (JsonNode) pofData.get(POF).getContent();
        cutByAge = cutByAge.findValue("agegroups");

        switch (age) {
            case TARPPO:
                return cutByAge.get(3).findValue("taskgroups");
            case TARPPODEV:
                return cutByAge.get(3).findValue("taskgroups");
            default:
                throw new IllegalArgumentException("no agegroup found for:" + age);
        }
    }

    //above method gives list of taskgroups we need so we go into double loop
    private ArrayNode getTasksFromTaskGroups(List<JsonNode> taskGroups) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode activities = mapper.createArrayNode();
        long atStart = System.currentTimeMillis();
        for (JsonNode taskGroup : taskGroups) {
            for (JsonNode taskShallowDescription : taskGroup) {
                JsonNode task = createFrontEndActivity(taskShallowDescription);
                if (task != null) {
                    activities.add(task);
                }
            }
        }
        //20sec-1min on my laptop, curius too see how long travis or aws will take
        System.out.println("time used to parse: " + ((System.currentTimeMillis() - atStart) / 1000) + "sec");
        return activities;
    }

    //fetch the link
    private JsonNode createFrontEndActivity(JsonNode taskShallowDescription) {
        try {
            URI detailUrl = URI.create(taskShallowDescription.findValue("languages").findValue("details").asText());
            JsonNode task = restTemplate.getForObject(detailUrl, JsonNode.class);

            return writeCustomActivity(task);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    //write the activity for frontend
    //put puts a value, set puts a node, putting a node is deprecated
    private JsonNode writeCustomActivity(JsonNode task) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode activity = mapper.createObjectNode();

        activity.set("guid", task.findValue("guid"));
        activity.set("title", task.findValue("title"));
        activity.set("content", task.findValue("content"));
        if (task.findValue("pakollisuus").findValue("name").asText().toLowerCase().equals("pakollinen")) {
            activity.put("mandatory", true);
        } else {
            activity.put("mandatory", false);
        }
        activity.set("place", task.findValue("paikka").findValue("name"));
        activity.set("duration", task.findValue("suoritus_kesto").findValue("name"));
        activity.set("task_term", task.findValue("task_term").findValue("name"));

        //lists are made like this into objectnode
        ArrayNode growthArray = activity.putArray("kasvatustavoitteet");
        growthArray.addAll(task.findValue("kasvatustavoitteet").findValues("name"));
        ArrayNode skillArray = activity.putArray("taitoalueet");
        if (task.findValue("taitoalueet") != null) {
            skillArray.addAll(task.findValue("taitoalueet").findValues("name"));
        }
        ArrayNode suggestionArray = activity.putArray("suggestions");
        if (task.findValue("suggestions_details").findValue("details") != null) {
            URI detailUrl = URI.create(task.findValue("suggestions_details").findValue("details").asText());
            JsonNode suggestionsIn = restTemplate.getForObject(detailUrl, JsonNode.class);         
            suggestionArray.addAll(suggestionsIn.findValue("items").findValues("content"));
        }
        activity.set("originUrl", task.findValue("languages").findValue("details"));

        return activity;
    }
}
