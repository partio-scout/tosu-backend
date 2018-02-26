package partio.jsonconfig;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import partio.domain.Activity;
import partio.domain.Event;
import partio.domain.EventGroup;

public class EventDeserializer extends StdDeserializer<Event> {

    public EventDeserializer() {
        this(null);
    }

    public EventDeserializer(Class<Event> t) {
        super(t);
    }

    @Override
    public Event deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {

        JsonNode node = jp.getCodec().readTree(jp);
        ObjectMapper mapper = new ObjectMapper();

        String title = node.get("title").asText();
        LocalDate startDate = LocalDate.parse(node.get("startDate").asText());
        LocalDate endDate = LocalDate.parse(node.get("endDate").asText());
        LocalTime startTime = LocalTime.parse(node.get("startTime").asText());
        LocalTime endTime = LocalTime.parse(node.get("endTime").asText());

        String type = node.get("type").asText();
        String information = "";

        if (node.get("information") != null) {
            information = node.get("information").asText();
        }

        List<Activity> activities = null;
        if (node.get("activities") != null) {
            activities = mapper.readValue(node.get("activities").asText(),
                    mapper.getTypeFactory().constructCollectionType(List.class, Activity.class));
        }
        EventGroup groupId = null;
        if (node.get("groupId") != null) {
            groupId = mapper.readValue(node.get("groupId").asText(), EventGroup.class);
        }
        return new Event(title, startDate, endDate, startTime, endTime, type, information, groupId, activities);
    }

}
