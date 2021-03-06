package partio.controller;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import partio.domain.Activity;
import partio.domain.Scout;
import partio.repository.ActivityBufferRepository;
import partio.repository.VerifyScoutService;
import partio.service.ActivityService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Scope(value = "session")
public class ActivityController {

    @Autowired
    private ActivityService activityService;
    @Autowired
    private VerifyScoutService verifyScoutService;
    @Autowired 
    private ActivityBufferRepository bufferRepository;

    @DeleteMapping("/activities/{activityId}")
    public ResponseEntity<Object> deleteActivity(@PathVariable Long activityId, HttpSession session) {
        Scout scout = (Scout) session.getAttribute("scout");
        if (!verifyScoutService.isOwnerForActivity(activityId, scout)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("you are not owner of this activity!");
        }
        return activityService.removeActivity(activityId);
    }

    @PostMapping("/events/{eventId}/activities")
    public ResponseEntity<Object> postActivity(@PathVariable Long eventId, @RequestBody Activity jsonActivity, HttpSession session) {
        Scout scout = (Scout) session.getAttribute("scout");
        if (!verifyScoutService.isLoggedIn(scout)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("you are not logged in!");
        }
        return activityService.addActivity(eventId, jsonActivity);
    }

    @GetMapping("/activities")
    public ResponseEntity<Object> getActivity(HttpSession session) {
        Scout user = (Scout) session.getAttribute("scout");
        if (!verifyScoutService.isLoggedIn(user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("you are not logged in!");
        }
        return ResponseEntity.ok(activityService.listActivitiesForUser(user));
    }

    //not restful methods that make life on frontend easy
    
    @PutMapping("/activity/{activityId}/fromevent/{eventId}/tobuffer")
    public ResponseEntity<Object> moveActivityFromEventToBuffer(@PathVariable Long activityId,
            @PathVariable Long eventId,
            HttpSession session) {
        
            Scout scout = (Scout) session.getAttribute("scout");
            
            if (!verifyScoutService.isOwnerForActivity(activityId, scout)
                    && !verifyScoutService.isOwnerForEvent(eventId, scout)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("you are not owner of these!");
            }

            return activityService.moveActivityFromEventToBuffer(activityId, eventId, bufferRepository.findByScout(scout));

    }

    @PutMapping("/activity/{activityId}/frombuffer/toevent/{eventId}")
    public ResponseEntity<Object> moveActivityFromBufferToEvent(@PathVariable Long activityId,
            @PathVariable Long eventId,
            HttpSession session) {
        Scout scout = (Scout) session.getAttribute("scout");

        if (!verifyScoutService.isOwnerForActivity(activityId, scout)
                && !verifyScoutService.isOwnerForEvent(eventId, scout)) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("you are not owner of these!");
        }

        return activityService.moveActivityFromBufferToEvent(activityId, eventId, bufferRepository.findByScout(scout));
    }

    @PutMapping("/activity/{activityId}/fromevent/{fromId}/toevent/{toId}")
    public ResponseEntity<Object> moveActivityFromEventToOtherEvent(@PathVariable Long activityId,
            @PathVariable Long fromId,
            @PathVariable Long toId,
            HttpSession session) {

        Scout scout = (Scout) session.getAttribute("scout");

        if (!verifyScoutService.isOwnerForActivity(activityId, scout)
                && !verifyScoutService.isOwnerForEvent(fromId, scout)
                && !verifyScoutService.isOwnerForEvent(toId, scout)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("you are not owner of these!");
        }
        return activityService.moveActivityFromEventToOtherEvent(activityId, fromId, toId);
    }
}
