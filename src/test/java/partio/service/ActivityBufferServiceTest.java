package partio.service;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import partio.domain.Activity;
import partio.domain.ActivityBuffer;
import partio.repository.ActivityBufferRepository;
import partio.repository.ActivityRepository;
import partio.repository.EventRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityBufferServiceTest {

    @Autowired
    private WebApplicationContext webAppContext;
    @Autowired
    private ActivityBufferService bufferService;
    @Autowired
    private EventRepository eventRepo;
    @Autowired
    private ActivityRepository activityRepo;
    @Autowired
    private ActivityBufferRepository bufferRepository;

    private MockMvc mockMvc;
    private Activity activity;
    private ActivityBuffer buffer;

    @Before
    public void setUp() {
        activityRepo.deleteAll();
        bufferRepository.deleteAll();

        activity = new Activity();
        buffer = new ActivityBuffer();

        activity.setGuid("testguid");

        activityRepo.save(activity);
        bufferRepository.save(buffer);
    }

    @After
    public void tearDown() {
        activityRepo.deleteAll();
        bufferRepository.deleteAll();
    }

    @Test
    public void testFindBuffer() {
        assertEquals(bufferService.findBuffer(buffer.getId()), buffer);
    }

    @Test
    public void testGetBufferContent() {
        assertEquals(bufferService.getBufferContent(buffer.getId()), ResponseEntity.ok(buffer));
    }

    @Test
    public void testAddActivity() {
        bufferService.addActivity(buffer.getId(), activity);
        assertEquals(activity.getBuffer(), buffer);
    }

}
