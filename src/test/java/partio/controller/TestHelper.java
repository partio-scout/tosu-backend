package partio.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import partio.domain.Activity;
import partio.domain.ActivityBuffer;
import partio.domain.Event;

public class TestHelper {

    private StringBuilder sb;

    public TestHelper() {
        sb = new StringBuilder();
    }

    public String activityToJson(Activity activity) {
        return "{ \"guid\":\"" + activity.getGuid() + "\"}";
    }

    public String eventToJson(Event event) {
        String json = "{ \"title\":\"" + event.getTitle() + "\", "
                + "\"startDate\":\"" + eventDateFormat(event.getStartDate()) + "\","
                + "\"endDate\":\"" + eventDateFormat(event.getEndDate()) + "\","
                + "\"startTime\":\"" + eventTimeFormat(event.getStartTime()) + "\","
                + "\"endTime\":\"" + eventTimeFormat(event.getEndTime()) + "\","
                + "\"type\":\"" + event.getType() + "\",";

        if (event.getGroupId() != null) {
            json += "\"groupId\":\"" + event.getGroupId().getId() + "\",";
        }

        json += "\"information\":\"" + event.getInformation() + "\"}";
        return json;
    }

    public String responseExpectedToContain(Event posted) {
        return "\"title\":\"" + posted.getTitle() + "\","
                + "\"startDate\":\"" + eventDateFormat(posted.getStartDate()) + "\","
                + "\"endDate\":\"" + eventDateFormat(posted.getEndDate()) + "\","
                + "\"startTime\":\"" + eventTimeFormat(posted.getStartTime()) + "\","
                + "\"endTime\":\"" + eventTimeFormat(posted.getEndTime()) + "\","
                + "\"type\":\"" + posted.getType() + "\","
                + "\"information\":\"" + posted.getInformation() + "\"";
    }

//    public String responseExpectedToContainActivity(Activity posted) {
//        return "\"guid\":\"" + posted.getGuid() + "\","
//                + "\"buffer\":\"" + posted.getBuffer() + "\"";
//    }
    public String eventDateFormat(LocalDate date) {
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

    public String eventTimeFormat(LocalTime time) {
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

    public String bufferToJson(ActivityBuffer buffer) {
        return "\"id\":\""+buffer.getId()+"\","
                + "\"activities\": []";                
    }

}
