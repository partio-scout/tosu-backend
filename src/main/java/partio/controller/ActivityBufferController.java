package partio.controller;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import partio.domain.Activity;
import partio.domain.Scout;
import partio.repository.VerifyScoutService;
import partio.service.ActivityBufferService;

@RestController
@Scope(value = "session")
public class ActivityBufferController {

    @Autowired
    private ActivityBufferService bufferService;
    @Autowired
    private VerifyScoutService verifyScoutService;

    @PostMapping("/activitybuffer/{bufferId}/activities/")
    public ResponseEntity<Object> postActivity(@PathVariable Long bufferId, @RequestBody Activity activity, HttpSession session) {
        Scout scout = (Scout) session.getAttribute("scout");
        System.out.println(scout + " in controller");
        if (!verifyScoutService.isOwnerForBuffer(bufferId, scout)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("you are not owner of this buffer!");
        }
        System.out.println("before service");
        return bufferService.addActivityToBuffer(activity, scout);
    }

    @GetMapping("/activitybuffer/{id}")//id voi poistaa
    public ResponseEntity<Object> getBufferContent(@PathVariable Long id, HttpSession session) {
        Scout scout = (Scout) session.getAttribute("scout");
        if (verifyScoutService.isLoggedIn(scout)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("you are not logged in!");
        }
        return bufferService.getBufferOfScout(scout);
    }
}
