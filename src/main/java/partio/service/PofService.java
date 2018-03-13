package partio.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
public class PofService extends RestTemplate {

    private final URI uri;
    private RestTemplate restTemplate;

    private LocalDate lastUpdatePof;
    private LocalDate lastUpdateTarppo;
    private ObjectNode rawpofData;
    private ArrayNode tarppoData;

    //   all/old pof
    public PofService() {
        this.restTemplate = new RestTemplate();
        this.uri = URI.create("https://pof-backend.partio.fi/spn-ohjelma-json-taysi/?postGUID=86b5b30817ce3649e590c5059ec88921");
        lastUpdatePof = LocalDate.MIN;
        lastUpdateTarppo = LocalDate.MIN;
        try {
            getPof();
         //   getTarppo(); this slows down starting the app a lot, uncomment when in actual production with real users
        } catch (IOException ex) {
            System.err.println("initializing pof failed");
        }
    }

    public ObjectNode getPof() throws IOException {
        if (lastUpdatePof != LocalDate.now()) {
            lastUpdatePof = LocalDate.now();
            rawpofData = fetchDataFromPof();
        }
        return rawpofData;
    }

    public ArrayNode getTarppo() throws IOException {
        //uses pof so it has to be updated as well
        if (!lastUpdatePof.equals(LocalDate.now())) {
            lastUpdatePof = LocalDate.now();
            rawpofData = fetchDataFromPof();
        }
        if (!lastUpdateTarppo.equals(LocalDate.now())) {
            lastUpdateTarppo = LocalDate.now();
            tarppoData = getPofActivitiesOfAge("tarppo");
        }
        return tarppoData;
    }

    private ObjectNode fetchDataFromPof() throws IOException {
        String json = restTemplate.getForObject(uri, String.class);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, ObjectNode.class);
    }

    //new pof by agegroup
    private ArrayNode getPofActivitiesOfAge(String age) throws IOException {
        //get the agegroup of choice ONLY TARPPO WORKS right now
        JsonNode pofOfAge = getTaskGroupsOfAge(age);
        //below returns tasks separated by taskgroups, so it's an array that has only tasks in them
        List<JsonNode> taskGroups = pofOfAge.findValues("tasks");
        //here goes into forloop and visits every url of each task
        //to create our custom activity for frontend easy to read
        ArrayNode customTasks = getTasksFromTaskGroups(taskGroups);

        return customTasks;
    }

    //only tarppo for now
    private JsonNode getTaskGroupsOfAge(String age) {
        JsonNode cutByAge = rawpofData
                .findValue("agegroups");

        switch (age) {
            case "tarppo":
                return cutByAge.get(3).findValue("taskgroups");
            default:
                throw new IllegalArgumentException("no agegroup found for:"+age);
        }
    }

    //above method gives list of taskgroups we need so we go into double loop
    private ArrayNode getTasksFromTaskGroups(List<JsonNode> taskGroups) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode activities = mapper.createArrayNode();
        int i = 0;
        long atStart = System.currentTimeMillis();
        for (JsonNode taskGroup : taskGroups) {
            for (JsonNode taskShallowDescription : taskGroup) {
                i++;
                JsonNode task = createFrontEndActivity(taskShallowDescription);
                if (task != null) {
                    activities.add(task);
                }
            }
        }
        //20sec-1min on my laptop, curius too see how long travis or aws will take
        System.out.println("tasks total: " + i + " successfully parsed: " + activities.size()
                + "\n time:" + ((System.currentTimeMillis() - atStart) / 1000) + "sec");
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
        if (task.findValue("tags").findValue("pakollisuus").asText().equals("pakollisuus")) {
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
        activity.set("suggestionDetailsUrl", task.findValue("suggestions_details").findValue("details"));

        activity.set("originUrl", task.findValue("languages").findValue("details"));

        return activity;
    }
}
