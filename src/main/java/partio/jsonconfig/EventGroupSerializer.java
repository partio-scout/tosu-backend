
package partio.jsonconfig;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import partio.domain.EventGroup;

public class EventGroupSerializer extends StdSerializer<EventGroup> {

   public EventGroupSerializer() {
        this(null);
    }
   
    public EventGroupSerializer(Class<EventGroup> t) {
        super(t);
    }
 
    @Override
    public void serialize(
      EventGroup value, JsonGenerator jgen, SerializerProvider provider) 
      throws IOException, JsonProcessingException {
  
        jgen.writeStartObject();
        jgen.writeNumberField("groupId", value.getId());
        jgen.writeEndObject();
    }
        
}
