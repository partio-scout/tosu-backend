package partio.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import partio.domain.Activity;
import partio.domain.ActivityBuffer;
import partio.domain.Event;
import partio.domain.Scout;
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
    @Autowired
    private ScoutService scoutService;

    public List<Event> list(GoogleIdToken idToken) {
        Scout scout = scoutService.findScoutByGoogleId(idToken);
        List<Event> events = eventRepository.findByScout(scout);
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
// group id cannot be changed, activities changed by activitycontroller

    public ResponseEntity<Object> edit(Long eventId, Event editedEvent, GoogleIdToken idToken) {
        Scout scout = scoutService.findScoutByGoogleId(idToken);

        Event toEdit = eventRepository.findOne(eventId);

        if (toEdit.getScout().getGoogleId() == null ? scout.getGoogleId() != null : !toEdit.getScout().getGoogleId().equals(scout.getGoogleId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } //can't edit someone else events

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

    public ResponseEntity<Object> deleteById(Long eventId, GoogleIdToken idToken) {
        Scout scout = scoutService.findScoutByGoogleId(idToken);

        Event toDelete = eventRepository.findOne(eventId);

        if (toDelete == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (toDelete.getScout().getGoogleId() == null ? scout.getGoogleId() != null : !toDelete.getScout().getGoogleId().equals(scout.getGoogleId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } //can't remove someone else events

        ActivityBuffer buffer = bufferService.findBuffer(0l);
        Event deleted = moveEventActivitysToBuffer(toDelete, buffer);

        //jos on viimeinen eventti joka kuuluu ryhmään niin
        //poistetaan ryhmä ja cascaden avul se poistaa myös eventin
        if (deleted.getGroupId() != null && deleted.getGroupId().getEvents().size() == 1) {
            groupRepository.delete(deleted.getGroupId());
        } else {
            eventRepository.delete(deleted);
        }
        return ResponseEntity.ok(deleted);

    }

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
