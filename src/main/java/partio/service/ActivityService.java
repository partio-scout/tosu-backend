package partio.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import partio.domain.Activity;
import partio.domain.ActivityBuffer;
import partio.domain.Event;
import partio.repository.ActivityRepository;
import partio.repository.EventRepository;
import partio.service.validators.ActivityValidator;

@Service
@Transactional
public class ActivityService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private ActivityValidator validator;
    @Autowired
    private ActivityBufferService bufferService;

    public ResponseEntity<Object> addActivity(Long eventId, Activity activity) {
        Event event = eventRepository.findOne(eventId);
        if (event == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        List<String> errors = validator.validateNew(activity);
        //for when we move onto next step (right now only uniq activities are allowed)
        //another suggestion would be adding if activity is not finished 
        //can add same acitivity
        //  if (event.getActivities().contains(guid)) {
        //    errors.add("Event can't have same activity more than once.");
        //  }
        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        activity.setEvent(event);
        activityRepository.save(activity);
        event.getActivities().add(activity);
        eventRepository.save(event);
        return ResponseEntity.ok(activity);
    }

    public ResponseEntity<Object> removeActivity(Long activityId) {
        Activity toDelete = activityRepository.findOne(activityId);
        if (toDelete == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        activityRepository.delete(toDelete);
        return ResponseEntity.ok(toDelete);
    }

    public List<Activity> list() {
        return activityRepository.findAll();
    }

    //new stuff from here
    public ResponseEntity<Object> restfulPut(long id, Activity activity) {
        System.out.println(activity);
        Activity original = activityRepository.findOne(id);
        if (original == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<String> errors = validator.validateChanges(original, activity);
        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        activityRepository.save(activity);
        return ResponseEntity.ok(activity);
    }

    public ResponseEntity<Object> moveActivityFromEventToBuffer(Long activityId, Long eventId, Long activityBufferId) {
        Activity activity = activityRepository.findOne(activityId);
        if (activity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Event from = eventRepository.findOne(eventId);
        ActivityBuffer to = bufferService.findBuffer(eventId);
        if (!from.getActivities().contains(activity)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        activity.setBuffer(to);
        activity.setEvent(null);
        List<String> errors = validator.validateChanges(activityRepository.findOne(activityId), activity);
        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        activityRepository.save(activity);

        return ResponseEntity.ok(activity);
    }

    public ResponseEntity<Object> moveActivityFromBufferToEvent(Long activityId, Long activityBufferId, Long eventId) {
        Activity activity = activityRepository.findOne(activityId);
        if (activity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Event to = eventRepository.findOne(eventId);
        ActivityBuffer from = bufferService.findBuffer(eventId);

        if (!from.getActivities().contains(activity)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        activity.setBuffer(null);
        activity.setEvent(to);
        List<String> errors = validator.validateChanges(activityRepository.findOne(activityId), activity);
        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        activityRepository.save(activity);

        return ResponseEntity.ok(activity);
    }

}
