package partio.controller;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import partio.domain.Activity;
import partio.domain.Scout;
import partio.service.ActivityBufferService;

@RestController
@Scope(value = "session")
public class ActivityBufferController {

    @Autowired
    private ActivityBufferService bufferService;
    
    
    @PostMapping("/activitybuffer/{id}/activities/")
    public ResponseEntity<Object> postActivity(@PathVariable Long id, @RequestBody Activity activity, HttpSession session) {
       Scout scout = (Scout) session.getAttribute("scout");
        return bufferService.addActivity(id, activity);
    }

    @GetMapping("/activitybuffer/{id}")
     
    public ResponseEntity<Object> getBufferContent(@PathVariable Long id, HttpSession session) {
        Scout scout = (Scout) session.getAttribute("scout");
        return bufferService.getBufferContent(id);
    }
}
