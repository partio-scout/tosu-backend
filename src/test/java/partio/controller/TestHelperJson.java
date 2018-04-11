package partio.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import partio.domain.Activity;
import partio.domain.ActivityBuffer;
import partio.domain.Event;
import partio.domain.Plan;

public class TestHelperJson {

    private StringBuilder stringBuilder;

    public TestHelperJson() {
        stringBuilder = new StringBuilder();
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

    public String planToJson(Plan plan) {
        String json = "{ \"title\":\"" + plan.getTitle() + "\", "
                + "\"id\":\"" + plan.getId() + "\","
                + "\"guid\":\"" + plan.getGuid() + "\",";
                

        if (plan.getActivity() != null) {
            json += "\"activityId\":\"" + plan.getActivity().getId() + "\",";
        }
        json += "\"content\":\"" + plan.getContent() + "\"}";

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
    
    public String eventDateFormat(LocalDate date) {
        stringBuilder.setLength(0); //clear sb

        stringBuilder.append(date.getYear()).append('-'); //year

        if (date.getMonthValue() < 10) {
            stringBuilder.append('0'); //month
        }
        stringBuilder.append(date.getMonthValue()).append('-');

        if (date.getDayOfMonth() < 10) {
            stringBuilder.append('0'); //day
        }
        stringBuilder.append(date.getDayOfMonth());

        return stringBuilder.toString();
    }

    public String eventTimeFormat(LocalTime time) {
        stringBuilder.setLength(0);

        if (time.getHour() < 10) {
            stringBuilder.append('0');
        }
        stringBuilder.append(time.getHour()).append(':');
        if (time.getMinute() < 10) {
            stringBuilder.append('0');
        }
        stringBuilder.append(time.getMinute());
        return stringBuilder.toString();
    }

    public String bufferToJson(ActivityBuffer buffer) {
        return "\"id\":\"" + buffer.getId() + "\","
                + "\"activities\": []";
    }

}
