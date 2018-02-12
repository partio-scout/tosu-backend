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

    public Activity addActivity(Long eventId, Activity activity) {

        Event event = eventRepository.getOne(eventId);
        activity.setEventid(eventId);
        activity.setEvent(event);
        activityRepository.save(activity);

        event.getActivities().add(activity);
        eventRepository.save(event);
        return activity;
    }

    public Activity removeActivity(Long activityId) {
        Activity activity = activityRepository.getOne(activityId);
        activityRepository.delete(activity);
        return activity;
    }

}
