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
    public void add(String name, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, String description) {
        Event event = new Event();
        event.setName(name);
        event.setStartDate(startDate);
        event.setEndDate(endDate);
        event.setStartTime(startTime);
        event.setEndTime(endTime);
        event.setDescription(description);

        eventRepository.save(event);
    }

}
