package partio.controller;

import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import partio.domain.Activity;
import partio.domain.Scout;
import partio.repository.ScoutRepository;
import partio.repository.VerifyScoutService;
import partio.service.ActivityService;

@RestController
@Scope(value = "session")
public class ActivityController {

    @Autowired
    private ActivityService activityService;
    @Autowired
    private ScoutRepository scoutRepository;
    @Autowired
    private VerifyScoutService verifyScoutService;

    @DeleteMapping("/activities/{activityId}")
    public ResponseEntity<Object> deleteActivity(@PathVariable Long activityId, HttpSession session) {
        Scout scout = (Scout) session.getAttribute("scout");
        if (verifyScoutService.isOwnerForActivity(activityId, scout)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("you are not owner of this activity!");
        }
        return activityService.removeActivity(activityId);
    }

    @PostMapping("/events/{eventId}/activities")
    public ResponseEntity<Object> postActivity(@PathVariable Long eventId, @RequestBody Activity jsonActivity, HttpSession session) {
        Scout scout = (Scout) session.getAttribute("scout");
        if (verifyScoutService.isLoggedIn(scout)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("you are not logged in!");
        }
        return activityService.addActivity(eventId, jsonActivity);
    }

    @GetMapping("/activities")
    public ResponseEntity<Object> getActivity(HttpSession session) {
        Scout user = scoutRepository.findByGoogleId((String) session.getAttribute("scout"));
        if (verifyScoutService.isLoggedIn(user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("you are not logged in!");
        }
        return ResponseEntity.ok(activityService.listActivitiesForUser(user));        
    }

    //new stuff from here
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

    @PutMapping("/activity/{id}/fromevent/{fromId}/toevent/{toId}")
    public ResponseEntity<Object> moveActivityFromEventToOtherEvent(@PathVariable Long id,
            @PathVariable Long fromId,
            @PathVariable Long toId) {

        return activityService.moveActivityFromEventToOtherEvent(id, fromId, toId);
    }
}
