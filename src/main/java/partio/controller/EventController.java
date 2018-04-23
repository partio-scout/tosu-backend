package partio.controller;

import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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
import partio.service.EventService;

@RestController
public class EventController {

    @Autowired
    private EventService eventService;
    @Autowired
    private ScoutRepository scoutRepository;

    @GetMapping("/events")
    public List<Event> getEvents(HttpSession session) {
        Scout scout = scoutRepository.findByGoogleId((String) session.getAttribute("scout"));
        List<Event> events = eventService.listScoutsEvents(scout);
        return events;
    }

    @PostMapping("/events")
    public ResponseEntity<Object> postEvent(@RequestBody Event event, HttpSession session) {
        event.setScout(scoutRepository.findByGoogleId((String) session.getAttribute("scout")));
        ResponseEntity<Object> newEvent = eventService.add(event);
        System.out.println("end even post");
        return newEvent;
    }

    @PutMapping("/events/{eventId}")
    public ResponseEntity<Object> editEvent(@PathVariable Long eventId, @RequestBody Event event, HttpSession session) {
        Scout scout = scoutRepository.findByGoogleId((String) session.getAttribute("scout"));
        return eventService.edit(eventId, event, scout);
    }

    @DeleteMapping("/events/{eventId}")
    public ResponseEntity<Object> deleteEvent(@PathVariable Long eventId, HttpSession session) {        
        Scout scout = scoutRepository.findByGoogleId((String) session.getAttribute("scout"));           
        return eventService.deleteById(eventId, scout);
    }
}
