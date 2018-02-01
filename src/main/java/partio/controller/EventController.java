package partio.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
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
        eventService.add("mennyt tapahtuma", LocalDate.of(2016, 2, 4), LocalDate.of(2016, 2, 6), LocalTime.of(13, 0), LocalTime.of(15, 0),"Tämän ei pitäisi näkyä listalla");
        eventService.add("Alkanut tapahtuma", LocalDate.of(2018, 1, 13), LocalDate.of(2018, 5, 14), LocalTime.of(13, 0), LocalTime.of(15, 0),"Tämän pitäisi näkyä listalla");
        eventService.add("Tuleva tapahtuma", LocalDate.of(2019, 5, 13), LocalDate.of(2019, 5, 14), LocalTime.of(13, 0), LocalTime.of(15, 0),"Tämän pitäisi näkyä listalla");
    }

    @GetMapping("/comingEvents")
    public List<Event> getEvents() {
        for(Event e : eventService.list()){
            System.out.println(e.getName());
        }
        return eventService.list();
    }
}
