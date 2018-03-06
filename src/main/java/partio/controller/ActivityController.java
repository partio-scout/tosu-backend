
package partio.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import partio.domain.Activity;
import partio.service.ActivityService;

@RestController
public class ActivityController {
    
    @Autowired
    private ActivityService activityService;    
    
    @DeleteMapping("/activities/{activityId}")
    public ResponseEntity<Object> deleteActivity(@PathVariable Long activityId) {
        return activityService.removeActivity(activityId);
    }

    @PostMapping("/events/{eventId}/activities")
    public ResponseEntity<Object> postActivity(@PathVariable Long eventId, @RequestBody Activity jsonActivity) {
        return activityService.addActivity(eventId, jsonActivity);
    }

    @GetMapping("/activities")
    public List<Activity> getActivity() {
        return activityService.list();
    }
    
     //new stuff from here
    
    //this regular put is really hard to use because it requires event and buffer to be included
    //(put replaces old object) and having to write event and buffer in json is a bitch
    //after these put there are some not so restful but working ways to swap to/from event/buffer
    @PutMapping("/activity/{id}")
    public ResponseEntity<Object> put(@PathVariable long id, @RequestBody Activity activity) {      
        return activityService.restfulPut(id, activity);
    }
   
    @PutMapping("/activity/{id}/fromevent/{eventId}/tobuffer/{bufferId}")
    public ResponseEntity<Object> moveActivityFromEventToBuffer(@PathVariable Long id,
            @PathVariable Long eventId, 
            @PathVariable Long bufferId) {
        
        return activityService.moveActivityFromEventToBuffer(id, eventId, bufferId);
    }
    @PutMapping("/activity/{id}/frombuffer/{bufferId}/toevent/{eventId}")
    public ResponseEntity<Object> moveActivityFromBufferToEvent(@PathVariable Long id,
            @PathVariable Long bufferId, 
            @PathVariable Long eventId) {
        
        return activityService.moveActivityFromBufferToEvent(id, eventId, bufferId);
    }
}
