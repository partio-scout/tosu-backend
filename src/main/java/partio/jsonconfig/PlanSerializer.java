package partio.jsonconfig;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import partio.domain.Plan;

public class PlanSerializer extends StdSerializer<Plan> {

    public PlanSerializer() {
        this(null);
    }

    public PlanSerializer(Class<Plan> t) {
        super(t);
    }

    @Override
    public void serialize(
            Plan value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        jgen.writeStartObject();

        jgen.writeNumberField("id", value.getId());
        jgen.writeStringField("guid", value.getGuid());
        jgen.writeStringField("title", value.getTitle());
        jgen.writeStringField("content", value.getContent());
        if (value.getActivity() != null) {
            jgen.writeNumberField("activityId", value.getActivity().getId());
        } else {
            jgen.writeNumberField("activityId", null);
        }

        jgen.writeEndObject();
    }

}
