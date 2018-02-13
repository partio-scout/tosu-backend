package partio.controller;

import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import partio.domain.Event;
import partio.service.EventService;

@RestController
public class EventController {

    @Autowired
    private EventService eventService;

    @PostConstruct
    public void construct() {
        Event event = new Event();
        event.setTitle("Superleiri 2018");
        event.setInformation("leireillään porukalla ja paistellaan makkaraa");
        event.setType("leiri");
        //event.setStartDate("2018-03-11");
        //event.setEndDate("2018-03-15");
        //event.setStartTime("16:00");
        //event.setEndTime("10:00");
        eventService.add(event);
//        eventService.add("mennyt tapahtuma", LocalDate.of(2016, 2, 4), LocalDate.of(2016, 2, 6), LocalTime.of(13, 0), LocalTime.of(15, 0), "Tämän ei pitäisi näkyä listalla");
//        eventService.add("Alkanut tapahtuma", LocalDate.of(2018, 1, 13), LocalDate.of(2018, 5, 14), LocalTime.of(13, 0), LocalTime.of(15, 0), "Tämän pitäisi näkyä listalla");
//        eventService.add("Tuleva tapahtuma", LocalDate.of(2019, 5, 13), LocalDate.of(2019, 5, 14), LocalTime.of(13, 0), LocalTime.of(15, 0), "Tämän pitäisi näkyä listalla");
    }

    @GetMapping("/events")
    public List<Event> getEvents() {
        List<Event> events = eventService.list();
        return events;
    }

    @PostMapping("/events")
    public ResponseEntity<Object> postEvent(@RequestBody Event event) {     
        ResponseEntity<Object> newEvent = eventService.add(event);
        return newEvent;
    }

    @PostMapping("/events/{eventId}")
    public ResponseEntity<Object> editEvent(@PathVariable Long eventId, @RequestBody Event event) {
        return eventService.edit(eventId, event);
    }

    @DeleteMapping("/events/{eventId}")
    public ResponseEntity<Object> deleteBook(@PathVariable Long eventId) {
        return eventService.deleteById(eventId);
    }
}
