package partio.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import partio.domain.Activity;
import partio.domain.ActivityBuffer;
import partio.domain.Event;
import partio.domain.EventGroup;
import partio.repository.ActivityBufferRepository;
import partio.repository.ActivityRepository;
import partio.repository.EventGroupRepository;
import partio.repository.EventRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventGroupControllerTest {

    @Autowired
    private WebApplicationContext webAppContext;

    @Autowired
    private EventRepository eventRepo;
    @Autowired
    private ActivityRepository activityRepo;
    @Autowired
    private EventGroupRepository groupRepo;
    @Autowired
    private ActivityBufferRepository bufferRepo;

    private MockMvc mockMvc;
    private Event event;
    private TestHelperJson helper;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
        event = new Event("le stub", LocalDate.now(), LocalDate.now(), LocalTime.MAX, LocalTime.MAX, "stub type", "this is a valid stub");
        helper = new TestHelperJson();
    }
    
    @After
    public void clean() {
        activityRepo.deleteAll();
        eventRepo.deleteAll();
        groupRepo.deleteAll();
    }

    @Test
    public void groupPost() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/eventgroup")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        Assert.assertTrue(groupRepo.findAll().size() == 1);
    }

    @Test
    public void groupDeleteDeletesEventsToo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/eventgroup")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        EventGroup group = groupRepo.findAll().get(0);
        event.setGroupId(group);

        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(helper.eventToJson(event)))
                .andExpect(status().isOk());
        
        mockMvc.perform(MockMvcRequestBuilders.delete("/eventgroup/"+ group.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
        
        Assert.assertTrue(groupRepo.findAll().isEmpty());
        Assert.assertTrue(eventRepo.findAll().isEmpty());

    }
    
    @Test
    public void groupDeleteDeletesEventsAndMovesActivtitiesToBufferToo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/eventgroup")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        EventGroup group = groupRepo.findAll().get(0);
        event.setGroupId(group);

        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(helper.eventToJson(event)))
                .andExpect(status().isOk());
        
        Event createdEvent = eventRepo.findAll().get(0);
        Activity activity = new Activity("delgroupmockactivity");
        mockMvc.perform(MockMvcRequestBuilders.post("/events/"+createdEvent.getId()+"/activities")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(helper.activityToJson(activity)))
                .andExpect(status().isOk());
        Activity createdActivity = activityRepo.findAll().get(0);
        Assert.assertTrue(Objects.equals(createdActivity.getEvent().getId(), createdEvent.getId()));
        
        mockMvc.perform(MockMvcRequestBuilders.delete("/eventgroup/"+ group.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
        
        ActivityBuffer shouldBeIn = bufferRepo.findAll().get(0);
        Assert.assertTrue(Objects.equals(shouldBeIn.getActivities().get(0).getId(), createdActivity.getId()));

        
        Assert.assertTrue(groupRepo.findAll().isEmpty());
        Assert.assertTrue(eventRepo.findAll().isEmpty());
        
        activityRepo.deleteAll();
        bufferRepo.deleteAll();

    }
    
    @Test
    public void groupDelete404WhenInvalid() throws Exception {
        
        mockMvc.perform(MockMvcRequestBuilders.delete("/eventgroup/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());

    }

}
