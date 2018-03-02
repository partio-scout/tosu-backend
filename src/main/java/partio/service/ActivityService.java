package partio.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import partio.domain.Activity;
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

}
