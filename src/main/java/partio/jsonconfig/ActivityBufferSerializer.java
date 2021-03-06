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

        jgen.writeNumberField("id", buffer.getId());
        jgen.writeArrayFieldStart("activities");

        if (buffer.getActivities() != null) {
            for (Activity activity : buffer.getActivities()) {                
                jgen.writeObject(activity);
            }
        }
        jgen.writeEndArray();

        jgen.writeEndObject();
    }

}
