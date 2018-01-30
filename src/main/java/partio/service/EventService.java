package partio.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import partio.domain.Event;
import partio.repository.EventRepository;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List<Event> list() {
        List<Event> events = eventRepository.findByEndTimeLessThan(LocalDateTime.now());
        return events;
    }

    @Transactional
    public void add(String name, LocalDateTime startTime, LocalDateTime endTime, String description) {
        Event event = new Event();
        event.setName(name);
        event.setStartTime(startTime);
        event.setEndTime(endTime);
        event.setDescription(description);

        eventRepository.save(event);
    }

}
