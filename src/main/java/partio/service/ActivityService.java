package partio.service;

import org.springframework.beans.factory.annotation.Autowired;
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

    public Activity addActivity(Long eventId, Activity a) {

        Event event = eventRepository.findOne(eventId);
        a.setEventid(eventId);
        a.setEvent(event);
        activityRepository.save(a);

        event.getActivities().add(a);
        eventRepository.save(event);
        return a;
    }

    public Activity removeActivity(Long activityId) {
        Activity activity = activityRepository.findOne(activityId);
        activityRepository.delete(activity);
        return activity;
    }

}
