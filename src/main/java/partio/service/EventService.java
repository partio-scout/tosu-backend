package partio.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import partio.domain.Event;
import partio.repository.EventRepository;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List<Event> list() {
        List<Event> events = eventRepository.findByEndDateAfter(LocalDate.now());
        return events;
    }

    @Transactional
    public void add(String title, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, String type, String information) {
        Event event = new Event();
        event.setTitle(title);
        event.setStartDate(startDate);
        event.setStartTime(startTime);
        event.setEndDate(endDate);        
        event.setEndTime(endTime);
        event.setType(type);
        event.setInformation(information);

        eventRepository.save(event);
    }

    @Transactional
    public void addActivity(Long eventId, String activity) {
        Event event = eventRepository.findOne(eventId);
        List<String> activities = event.getActivities();
        activities.add(activity);
        event.setActivities(activities);
        eventRepository.save(event);
    }

    @Transactional
    public void removeActivity(Long eventId, String activity) {
        Event event = eventRepository.findOne(eventId);
        List<String> activities = event.getActivities();
        activities.remove(activity);
        event.setActivities(activities);
        eventRepository.save(event);
    }

}
