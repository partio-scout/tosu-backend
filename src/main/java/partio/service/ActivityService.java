package partio.service;

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
import partio.domain.Plan;
import partio.domain.Scout;
import partio.repository.ActivityBufferRepository;
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
    private ActivityBufferRepository bufferRepository;

    /*
    Add new activiy to event.
     */
    public ResponseEntity<Object> addActivity(Long eventId, Activity activity) {
        Event event = eventRepository.findOne(eventId);
        if (event == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        activity.setEvent(event);
        List<String> errors = validator.validateNew(activity);

        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        List<Plan> plans = new ArrayList<>();
        activity.setPlans(plans);
        activityRepository.save(activity);
        event.getActivities().add(activity);
        eventRepository.save(event);
        return ResponseEntity.ok(activity);
    }

    /*
    Remove activity from event.
     */
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

    /*
    Change position of activity from event to activitybuffer.
     */
    public ResponseEntity<Object> moveActivityFromEventToBuffer(Long activityId, Long eventId, Long activityBufferId) {
        Activity activity = activityRepository.findOne(activityId);
        if (activity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Event from = eventRepository.findOne(eventId);
        ActivityBuffer to = bufferRepository.findOne(activityBufferId);
        if ((from == null || from.getActivities() == null || !from.getActivities().contains(activity)) || to == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (to.getActivities() != null && to.getActivities().size() >= ActivityBuffer.BUFFER_SIZE) {
            ArrayList<String> errors = new ArrayList<>();
            errors.add("filter is full");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        activity.setBuffer(to);
        activity.setEvent(null);
        activityRepository.save(activity);//it already exists so no need to validate

        return ResponseEntity.ok(activity);
    }

    /*
    Change position of activity from activitybuffer to event.
     */
    public ResponseEntity<Object> moveActivityFromBufferToEvent(Long activityId, Long eventId, Long activityBufferId) {
        //                                                   .moveActivityFromBufferToEvent(id, eventId, bufferId);
        Activity activity = activityRepository.findOne(activityId);
        if (activity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Event to = eventRepository.findOne(eventId);
        ActivityBuffer from = bufferRepository.findOne(activityBufferId);

        if (from == null || from.getActivities() == null || !from.getActivities().contains(activity)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        if (to == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        activity.setBuffer(null);
        activity.setEvent(to);//it already exists so no need to validate
        activityRepository.save(activity);

        return ResponseEntity.ok(activity);
    }

    /*
    Change position of activity from event to other event.
     */
    public ResponseEntity<Object> moveActivityFromEventToOtherEvent(Long activityId, Long eventIdFrom, Long eventIdTo) {
        Activity activity = activityRepository.findOne(activityId);
        if (activity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Event to = eventRepository.findOne(eventIdTo);
        Event from = eventRepository.findOne(eventIdFrom);

        if (from == null || from.getActivities() == null || !from.getActivities().contains(activity)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        activity.setEvent(to);//it already exists so no need to validate
        activityRepository.save(activity);
        return ResponseEntity.ok(activity);
    }

    /*
    List current user's activities.
     */
    public List<Activity> listActivitiesForUser(Scout user) {
        List<Activity> activitiesOfUser = new ArrayList<>();
        if (!user.getEvents().isEmpty()) {
            user.getEvents().forEach(event -> {
                if (!event.getActivities().isEmpty()) {
                    activitiesOfUser.addAll(event.getActivities());
                }
            });
        }

        return activitiesOfUser;
    }

}
