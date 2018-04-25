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
import partio.domain.Event;
import partio.domain.Scout;
import partio.repository.ScoutRepository;
import partio.repository.VerifyScoutService;
import partio.service.EventService;

@RestController
@Scope(value = "session")
public class EventController {

    @Autowired
    private EventService eventService;
    @Autowired
    private ScoutRepository scoutRepository;
    @Autowired
    private VerifyScoutService verifyScoutService;

    @GetMapping("/events")
    public List<Event> getEvents(HttpSession session) {
        Scout scout = scoutRepository.findByGoogleId((String) session.getAttribute("scout"));
        List<Event> events = eventService.listScoutsEvents(scout);
        return events;
    }

    @PostMapping("/events")
    public ResponseEntity<Object> postEvent(@RequestBody Event event, HttpSession session) {
        Scout loggedInScout = scoutRepository.findByGoogleId((String) session.getAttribute("scout"));
        if (verifyScoutService.isLoggedIn(loggedInScout)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("you are not logged in!");
        }
        event.setScout(loggedInScout);
        ResponseEntity<Object> newEvent = eventService.add(event);
        return newEvent;
    }

    @PutMapping("/events/{eventId}")
    public ResponseEntity<Object> editEvent(@PathVariable Long eventId, @RequestBody Event event, HttpSession session) {
        Scout loggedInScout = scoutRepository.findByGoogleId((String) session.getAttribute("scout"));
        if (verifyScoutService.isOwnerForEvent(eventId, loggedInScout)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("you are not owner of this event!");
        }
        return eventService.edit(eventId, event);
    }

    @DeleteMapping("/events/{eventId}")
    public ResponseEntity<Object> deleteEvent(@PathVariable Long eventId, HttpSession session) {
        Scout loggedInScout = scoutRepository.findByGoogleId((String) session.getAttribute("scout"));
        if (verifyScoutService.isOwnerForEvent(eventId, loggedInScout)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("you are not owner of this event!");
        }
        return eventService.deleteById(eventId);
    }
}
