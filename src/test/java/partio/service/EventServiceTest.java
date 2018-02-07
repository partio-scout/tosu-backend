package partio.service;

import java.util.List;
import junit.framework.Assert;
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
    EventService es;
    
    @Autowired
    EventRepository er;
    
    public void clean() {
        List<Event> evs = er.findAll();
        evs.forEach((ev) -> {
            er.delete(ev);
        });
    }

    @Test
    public void testPostEventsEnterDB() throws InterruptedException {
        clean();
        es.add(new Event());
        Thread.sleep(10);
        Assert.assertEquals(1, er.findAll().size());
        es.add(new Event());
        Assert.assertEquals(2, er.findAll().size());
        clean();
    }
}
