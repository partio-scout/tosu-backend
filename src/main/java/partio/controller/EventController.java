package partio.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import partio.domain.Event;
import partio.domain.Scout;
import partio.service.EventService;
import partio.service.ScoutService;

@RestController
public class EventController {

    @Autowired
    private EventService eventService;
    @Autowired
    private ScoutService scoutService;

    @GetMapping("/events")
    public List<Event> getEvents(@RequestHeader GoogleIdToken idToken) {
        List<Event> events = eventService.list(idToken);
        return events;
    }

    @PostMapping("/events")
    public ResponseEntity<Object> postEvent(@RequestBody Event event, @RequestHeader GoogleIdToken idToken) {   
        Scout scout = scoutService.findScoutByGoogleId(idToken);
        event.setScout(scout);
        ResponseEntity<Object> newEvent = eventService.add(event);
        return newEvent;
    }

    @PutMapping("/events/{eventId}")
    public ResponseEntity<Object> editEvent(@PathVariable Long eventId, @RequestBody Event event, @RequestHeader GoogleIdToken idToken) {
        return eventService.edit(eventId, event);
    }

    @DeleteMapping("/events/{eventId}")
    public ResponseEntity<Object> deleteEvent(@PathVariable Long eventId, @RequestHeader GoogleIdToken idToken) {
        return eventService.deleteById(eventId, idToken);
    }
}
