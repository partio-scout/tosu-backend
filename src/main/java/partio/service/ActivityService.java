package partio.service;

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

@Service
@Transactional
public class ActivityService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ActivityRepository activityRepository;

    public ResponseEntity<Object> addActivity(Long eventId, Activity activity) {

        Event event = eventRepository.findOne(eventId);
        if (event == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
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
