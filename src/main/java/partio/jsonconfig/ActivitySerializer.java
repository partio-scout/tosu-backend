
package partio.jsonconfig;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import partio.domain.Activity;

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
        jgen.writeStringField("information", value.getInformation());
        jgen.writeNumberField("eventId", value.getEvent().getId());
        jgen.writeEndObject();
    }
        
}
