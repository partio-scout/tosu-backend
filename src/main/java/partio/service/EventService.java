package partio.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import partio.domain.Event;
import partio.repository.EventRepository;
import partio.service.validators.EventValidator;

@Service
@Transactional
public class EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventValidator eventValidator;

    public List<Event> list() {
        List<Event> events = eventRepository.findAll();
        //List<Event> events = eventRepository.findByEndDateAfter(LocalDate.now());
        return events;
    }

    public ResponseEntity<Object> add(Event event) {
        List<String> errors = eventValidator.validateNew(event);

        if (errors.isEmpty()) {
            eventRepository.save(event);
            return ResponseEntity.ok(event);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.toString());
        }

    }

    //tää jäljellä
    public ResponseEntity<Object> edit(Long eventId, Event editedEvent) {
        Event original = eventRepository.findOne(eventId);
        List<String> errors = eventValidator.validateChanges(original, editedEvent);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.toString());

    }

    public ResponseEntity<Object> deleteById(Long eventId) {
        Event toDelete = eventRepository.findOne(eventId);
        if (toDelete == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            eventRepository.delete(toDelete);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
    }

}
