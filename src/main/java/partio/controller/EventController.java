package partio.controller;

import java.time.LocalDateTime;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import partio.domain.Event;
import partio.service.EventService;

@RestController
public class EventController {
    
    @Autowired
    private EventService eventService;


    @PostConstruct
    public void construct() {
        eventService.add("mennyt tapahtuma", LocalDateTime.of(1990, 5, 13, 3, 0), LocalDateTime.of(1990, 5, 14, 3, 0), "Tämän ei pitäisi näkyä listalla");
        eventService.add("Alkanut tapahtuma", LocalDateTime.of(2018, 1, 13, 3, 0), LocalDateTime.of(2018, 5, 14, 3, 0), "Tämän pitäisi näkyä listalla");
        eventService.add("Tuleva tapahtuma", LocalDateTime.of(2018, 5, 13, 3, 0), LocalDateTime.of(2018, 5, 14, 3, 0), "Tämän pitäisi näkyä listalla");
    }

    @GetMapping("/comingEvents")
    public List<Event> getEvents() {
        return eventService.list();
    }
}
