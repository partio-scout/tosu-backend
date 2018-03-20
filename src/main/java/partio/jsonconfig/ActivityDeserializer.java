package partio.jsonconfig;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.util.List;
import partio.domain.Activity;
import partio.domain.ActivityBuffer;
import partio.domain.Event;
import partio.domain.Plan;


//with plans in activity this might be needed not sure yet...
public class ActivityDeserializer extends StdDeserializer<Activity> {

    public ActivityDeserializer() {
        this(null);
    }

    public ActivityDeserializer(Class<Activity> t) {
        super(t);
    }

    @Override
    public Activity deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {

        JsonNode node = jp.getCodec().readTree(jp);
        ObjectMapper mapper = new ObjectMapper();

        Event event = null;
        if (node.get("event") != null) {
            event = mapper.convertValue(node.get("event"), Event.class);
        }
        ActivityBuffer buffer = null;
        if (node.get("buffer") != null) {
            buffer = mapper.convertValue(node.get("buffer"), ActivityBuffer.class);
        }

        List<Plan> plans = null;
        if (node.get("plans") != null) {
            plans = mapper.readValue(node.get("plans").asText(),
                    mapper.getTypeFactory().constructCollectionType(List.class, Plan.class));
        }
        String guid = null;
        if (node.get("guid") != null) {
            guid = node.get("guid").asText();
        }
        return new Activity(event, buffer, plans, guid);
    }

}
