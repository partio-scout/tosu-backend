package partio.service;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import partio.domain.Event;
import partio.repository.EventRepository;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class EventServiceTest {

    @Autowired
    EventService eventService;
    
    @Autowired
    EventRepository eventRepo;
    
    @After
    public void clean() {
        eventRepo.deleteAll();
    }

    @Test//we have added validation not good enough anymore
    public void testInvalidPostEventsEnterDB() throws InterruptedException {
        eventService.add(new Event());
        Assert.assertEquals(0, eventRepo.findAll().size());
        eventService.add(new Event());
        Assert.assertEquals(0, eventRepo.findAll().size());
    }
}
