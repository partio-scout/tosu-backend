package partio.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import partio.domain.Event;
import partio.repository.EventRepository;

@Service
@Transactional
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List<Event> list() {
        List<Event> events = eventRepository.findByEndDateAfter(LocalDate.now());
        return events;
    }

    public Event add(Event event) {
        eventRepository.save(event);
        return event;
    }

  
}
