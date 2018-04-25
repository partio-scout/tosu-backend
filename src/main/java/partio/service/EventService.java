package partio.service;

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
import partio.repository.EventGroupRepository;
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
    private EventGroupRepository groupRepository;
    @Autowired
    private ActivityBufferService bufferService;
    @Autowired
    private EventValidator eventValidator;

    /*
    List all events from database. Needed only at tests.
    */
    public List<Event> list() {
        List<Event> events = eventRepository.findAll(orderBy());
        return events;
    }

    private Sort orderBy() {
        return new Sort(
                new Order(Direction.ASC, "startDate"),
                new Order(Direction.ASC, "startTime"));
    }
    
    /*
    Add new event.
    */
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
    
    /*
    Edit event.
    */
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

    /*
    Delete event.
    */
    public ResponseEntity<Object> deleteById(Long eventId) {
        Event toDelete = eventRepository.findOne(eventId);
        if (toDelete == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        ActivityBuffer buffer = bufferService.findBuffer(0l);
        Event deleted = moveEventActivitysToBuffer(toDelete, buffer);

        if (deleted.getGroupId() != null && deleted.getGroupId().getEvents().size() == 1) {
            groupRepository.delete(deleted.getGroupId());
        } else {
            eventRepository.delete(deleted);
        }
        return ResponseEntity.ok(deleted);

    }
    
    /*
    When event is deleted move all its activities to bufferzone.
    */
    private Event moveEventActivitysToBuffer(Event event, ActivityBuffer buffer) {
        List<Activity> eventActivitys = event.getActivities();

        if (eventActivitys == null || eventActivitys.isEmpty()) {
            return event; //nothin to momve
        }
        System.out.println("proceed to move");
        if (buffer.getActivities() == null) {
            buffer.setActivities(new ArrayList<>());
        }
        for (Activity eventActivity : eventActivitys) {
            eventActivity.setBuffer(buffer);
            eventActivity.setEvent(null);
        }
        activityRepository.save(eventActivitys);

        return event;
    }
}