package partio.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import partio.domain.Event;
import partio.service.EventService;

@RestController
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/events")
    public Page<Event> getEvents() {
        Page<Event> events = eventService.list();
        return events;
    }

    @PostMapping("/events")
    public ResponseEntity<Object> postEvent(@RequestBody Event event) {     
        ResponseEntity<Object> newEvent = eventService.add(event);
        return newEvent;
    }

    @PutMapping("/events/{eventId}")
    public ResponseEntity<Object> editEvent(@PathVariable Long eventId, @RequestBody Event event) {
        return eventService.edit(eventId, event);
    }

    @DeleteMapping("/events/{eventId}")
    public ResponseEntity<Object> deleteEvent(@PathVariable Long eventId) {
        return eventService.deleteById(eventId);
    }
}
