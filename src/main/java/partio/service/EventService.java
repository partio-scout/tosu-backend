package partio.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import partio.domain.Activity;
import partio.domain.ActivityBuffer;
import partio.domain.Event;
import partio.repository.ActivityRepository;
import partio.repository.EventRepository;
import partio.service.validators.EventValidator;

@Service
@Transactional
public class EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private ActivityBufferService bufferService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private EventValidator eventValidator;

    public List<Event> list() {
        // List<Event> events = eventRepository.findAll();
        List<Event> events = eventRepository.findAll(orderBy());
        return events;
    }

    private Sort orderBy() {
        return new Sort(
                new Order(Direction.ASC, "startDate"),
                new Order(Direction.ASC, "startTime"));
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
// group id cannot be changed, activities changed by activitycontroller

    public ResponseEntity<Object> edit(Long eventId, Event editedEvent) {
        Event original = eventRepository.findOne(eventId);
        List<String> errors = eventValidator.validateChanges(original, editedEvent);

        if (errors.isEmpty()) {
            original.setVariables(editedEvent);
            eventRepository.save(original);
            return ResponseEntity.ok(original);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.toString());
        }
    }

    public ResponseEntity<Object> deleteById(Long eventId) {
        Event toDelete = eventRepository.findOne(eventId);
        if (toDelete == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        List<Activity> eventActivitys = toDelete.getActivities();
        if (eventActivitys != null) {
            eventActivitys.forEach((eventActivity) -> {
                activityService.moveActivityFromEventToBuffer(eventActivity.getId(), eventId, 0l);
            });
        }
        toDelete.setActivities(new ArrayList<>());
        eventRepository.delete(toDelete);
        return ResponseEntity.ok(toDelete);

    }
}
