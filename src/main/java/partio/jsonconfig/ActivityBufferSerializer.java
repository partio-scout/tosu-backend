package partio.jsonconfig;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.math.BigDecimal;
import partio.domain.Activity;
import partio.domain.ActivityBuffer;

public class ActivityBufferSerializer extends StdSerializer<ActivityBuffer> {

    public ActivityBufferSerializer() {
        this(null);
    }

    public ActivityBufferSerializer(Class<ActivityBuffer> t) {
        super(t);
    }

    @Override
    public void serialize(
            ActivityBuffer buffer, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        jgen.writeStartObject();
        try {
        if (buffer.getActivities() != null) {

            jgen.writeNumberField("id", buffer.getId());
            jgen.writeArrayFieldStart("activities");

            if (buffer.getActivities() != null) {

                for (Activity activity : buffer.getActivities()) {
                    jgen.writeStartObject();
                    jgen.writeNumberField("id", activity.getId());
                    jgen.writeStringField("guid", activity.getGuid());
                    jgen.writeEndObject();
                }
            }
            jgen.writeEndArray();
        } else {
            jgen.writeStringField("activities", null);
        }
        }catch (Exception e) {
            e.printStackTrace();
        }
        jgen.writeEndObject();
    }

}
