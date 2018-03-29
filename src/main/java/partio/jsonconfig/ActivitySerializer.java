
package partio.jsonconfig;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import partio.domain.Activity;
import partio.domain.Plan;

public class ActivitySerializer extends StdSerializer<Activity> {

   public ActivitySerializer() {
        this(null);
    }
   
    public ActivitySerializer(Class<Activity> t) {
        super(t);
    }
 
    @Override
    public void serialize(
      Activity value, JsonGenerator jgen, SerializerProvider provider) 
      throws IOException, JsonProcessingException {
  
        jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId());
        jgen.writeStringField("guid", value.getGuid());
        if (value.getEvent() != null) {
        jgen.writeNumberField("eventId", value.getEvent().getId());
        } else {
            jgen.writeNumberField("eventId", null);
        }
        if (value.getBuffer()!= null) {
        jgen.writeNumberField("bufferZoneId", value.getBuffer().getId());
        } else {
            jgen.writeNumberField("bufferZoneId", null);
        }
//        jgen.writeArrayFieldStart("plans");
//        if (value.getPlans() != null) {
//            for (Plan plan : value.getPlans()) {
//                jgen.writeObject(plan);
//            }
//        }
//        jgen.writeEndArray();
        jgen.writeEndObject();
    }
        
}
