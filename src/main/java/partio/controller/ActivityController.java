
package partio.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import partio.domain.Activity;
import partio.repository.ActivityRepository;
import partio.service.ActivityService;

@RestController
public class ActivityController {
    
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivityRepository activityRepository;
    
    
    @DeleteMapping("/activities/{activityId}")
    public Activity deleteActivity(@PathVariable Long activityId) {
        Activity activity = activityService.removeActivity(activityId);
        return activity;
    }

    @PostMapping("events/{eventId}/activities")
    public Activity postActivity(@PathVariable Long eventId, @RequestBody Activity jsonActivity) {
        Activity activity = activityService.addActivity(eventId, jsonActivity);
        return activity;
    }

    @GetMapping("/activities")
    public List<Activity> getActivity() {
        return activityRepository.findAll();
    }
    
}
