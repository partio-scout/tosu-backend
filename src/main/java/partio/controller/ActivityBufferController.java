package partio.controller;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import partio.domain.Activity;
import partio.domain.Scout;
import partio.repository.VerifyScoutService;
import partio.service.ActivityBufferService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Scope(value = "session")
public class ActivityBufferController {

    @Autowired
    private ActivityBufferService bufferService;
    @Autowired
    private VerifyScoutService verifyScoutService;

    @PostMapping("/activitybuffer/activities")
    public ResponseEntity<Object> postActivity(@RequestBody Activity activity, HttpSession session) {
        Scout scout = (Scout) session.getAttribute("scout");
        if (!verifyScoutService.isLoggedIn(scout)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("you are not logged in!");
        }
        
        return bufferService.addActivityToBuffer(activity, scout);
    }

    @GetMapping("/activitybuffer/{id}")//id voi poistaa
    public ResponseEntity<Object> getBufferContent(@PathVariable Long id, HttpSession session) {
        Scout scout = (Scout) session.getAttribute("scout");
        if (!verifyScoutService.isLoggedIn(scout)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("you are not logged in!");
        }
        return bufferService.getBufferOfScout(scout);
    }
}
