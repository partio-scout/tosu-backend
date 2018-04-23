package partio.jsonconfig;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import partio.domain.Activity;
import partio.domain.Event;

public class EventSerializer extends StdSerializer<Event> {

    private StringBuilder sb;

    public EventSerializer() {
        this(null);
        sb = new StringBuilder(16);
    }

    public EventSerializer(Class<Event> t) {
        super(t);
        sb = new StringBuilder(16);
    }

    @Override
    public void serialize(
            Event event, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        jgen.writeStartObject(); // {

        if (event.getId() != null) {
            jgen.writeNumberField("id", event.getId());
        }
        if (event.getScout()!= null) {
            jgen.writeNumberField("scoutId", event.getScout().getId());
        } else {
            jgen.writeNullField("scoutId");
        }
        jgen.writeStringField("title", event.getTitle());

        jgen.writeObjectField("startDate", eventDateFormat(event.getStartDate()));
        jgen.writeObjectField("endDate", eventDateFormat(event.getEndDate()));

        jgen.writeObjectField("startTime", eventTimeFormat(event.getStartTime()));
        jgen.writeObjectField("endTime", eventTimeFormat(event.getEndTime()));
        jgen.writeStringField("type", event.getType());
        jgen.writeStringField("information", event.getInformation());

        if (event.getGroupId() != null) {
            jgen.writeNumberField("groupId", event.getGroupId().getId());
        } else {
            jgen.writeNumberField("groupId", null);
        }

        jgen.writeArrayFieldStart("activities");
        if (event.getActivities() != null) {
            for (Activity activity : event.getActivities()) {
                jgen.writeObject(activity);
            }
        }
        jgen.writeEndArray();

        jgen.writeEndObject(); // }

    }

    private String eventDateFormat(LocalDate date) {
        sb.setLength(0); //clear sb

        sb.append(date.getYear()).append('-'); //year

        if (date.getMonthValue() < 10) {
            sb.append('0'); //month
        }
        sb.append(date.getMonthValue()).append('-');

        if (date.getDayOfMonth() < 10) {
            sb.append('0'); //day
        }
        sb.append(date.getDayOfMonth());

        return sb.toString();
    }

    private String eventTimeFormat(LocalTime time) {
        sb.setLength(0);

        if (time.getHour() < 10) {
            sb.append('0');
        }
        sb.append(time.getHour()).append(':');
        if (time.getMinute() < 10) {
            sb.append('0');
        }
        sb.append(time.getMinute());
        return sb.toString();
    }
}
