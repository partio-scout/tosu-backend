package partio.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import partio.domain.EventGroup;
import partio.domain.Scout;
import partio.repository.ScoutRepository;
import partio.repository.VerifyScoutService;
import partio.service.EventGroupService;

@RestController
@Transactional
public class EventGroupController {

    @Autowired
    private EventGroupService groupService;
    @Autowired
    private VerifyScoutService verifyScoutService;
    @Autowired
    private ScoutRepository scoutRepository;

    //creates a new group without params since it's only made of ids
    @PostMapping("/eventgroup")
    public ResponseEntity<Object> postEventGroup(HttpSession session) throws IOException {
        Scout loggedInScout = scoutRepository.findByGoogleId((String) session.getAttribute("scout"));
        if (verifyScoutService.isLoggedIn(loggedInScout)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("you are not logged in!");
        }
        return groupService.createEventGroup(loggedInScout);
    }

    //make it so it wont delete events that are completed if loggin is needed 
    //(logging is not yet needed so this will do)
    @DeleteMapping("/eventgroup/{groupId}")
    public ResponseEntity<Object> delete(@PathVariable Long groupId, HttpSession session) throws IOException {
        Scout loggedInScout = scoutRepository.findByGoogleId((String) session.getAttribute("scout"));
        if (verifyScoutService.isOwnerForEventGroup(groupId, loggedInScout)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("you are not owner of this event group!");
        }
        return groupService.delete(groupId);
    }
}
