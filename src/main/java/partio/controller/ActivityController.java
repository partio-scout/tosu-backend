
package partio.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import partio.domain.Activity;
import partio.service.ActivityService;

@RestController
public class ActivityController {
    
    @Autowired
    private ActivityService activityService;    
    
    //tämän
    @DeleteMapping("/activities/{activityId}")
    public ResponseEntity<Object> deleteActivity(@PathVariable Long activityId) {
        return activityService.removeActivity(activityId);
    }

    @PostMapping("events/{eventId}/activities")
    public ResponseEntity<Object> postActivity(@PathVariable Long eventId, @RequestBody Activity jsonActivity) {
        return activityService.addActivity(eventId, jsonActivity);
    }

    @GetMapping("/activities")
    public List<Activity> getActivity() {
        return activityService.list();
    }
    
}
