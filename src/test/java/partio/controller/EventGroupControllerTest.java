package partio.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
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
import partio.domain.Scout;
import partio.repository.ActivityBufferRepository;
import partio.repository.ActivityRepository;
import partio.repository.EventGroupRepository;
import partio.repository.EventRepository;
import partio.repository.ScoutRepository;

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
    @Autowired
    ScoutRepository scoutRepo;
    private Scout scout;

    private MockMvc mockMvc;
    private Event event;
    private TestHelperJson helper;
    Map<String, Object> sessionAttrs;

    @Before
    public void setUp() {
        scout = new Scout("mockid", null, null, "scout");
        scoutRepo.save(scout);
        ActivityBuffer buffer = new ActivityBuffer(null, scout);
        bufferRepo.save(buffer);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
        event = new Event("le stub", LocalDate.now(), LocalDate.now(), LocalTime.MAX, LocalTime.MAX, "stub type", "this is a valid stub", scout);
        helper = new TestHelperJson();

        sessionAttrs = new HashMap<>();
        sessionAttrs.putIfAbsent("scout", scout);
    }

    @After
    public void clean() {
        activityRepo.deleteAll();
        eventRepo.deleteAll();
        bufferRepo.deleteAll();
        groupRepo.deleteAll();
        scoutRepo.deleteAll();
        
    }

    @Test
    public void groupPost() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/eventgroup")
                .sessionAttrs(sessionAttrs)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        Assert.assertTrue(groupRepo.findAll().size() == 1);
    }
/*
    @Test
    public void groupDeleteDeletesEventsToo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/eventgroup")
                .sessionAttrs(sessionAttrs)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        EventGroup group = groupRepo.findAll().get(0);
        event.setGroupId(group);

        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                .sessionAttrs(sessionAttrs)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(helper.eventToJson(event)))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.delete("/eventgroup/" + group.getId())
                .sessionAttrs(sessionAttrs)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        Assert.assertTrue(groupRepo.findAll().isEmpty());
        Assert.assertTrue(eventRepo.findAll().isEmpty());

    }

    @Test
    public void groupDeleteDeletesEventsAndMovesActivtitiesToBufferToo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/eventgroup")
                .sessionAttrs(sessionAttrs)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        EventGroup group = groupRepo.findAll().get(0);
        event.setGroupId(group);

        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                .sessionAttrs(sessionAttrs)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(helper.eventToJson(event)))
                .andExpect(status().isOk());

        Event createdEvent = eventRepo.findAll().get(0);
        Activity activity = new Activity("delgroupmockactivity");
        mockMvc.perform(MockMvcRequestBuilders.post("/events/" + createdEvent.getId() + "/activities")
                .sessionAttrs(sessionAttrs)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(helper.activityToJson(activity)))
                .andExpect(status().isOk());
        Activity createdActivity = activityRepo.findAll().get(0);
        Assert.assertTrue(Objects.equals(createdActivity.getEvent().getId(), createdEvent.getId()));

        mockMvc.perform(MockMvcRequestBuilders.delete("/eventgroup/" + group.getId())
                .sessionAttrs(sessionAttrs)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        ActivityBuffer shouldBeIn = bufferRepo.findAll().get(0);
        Assert.assertTrue(Objects.equals(shouldBeIn.getActivities().get(0).getId(), createdActivity.getId()));

        Assert.assertTrue(groupRepo.findAll().isEmpty());
        Assert.assertTrue(eventRepo.findAll().isEmpty());
    }

    @Test
    public void groupDelete403whenInvalid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/eventgroup/1")
                .sessionAttrs(sessionAttrs)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
        mockMvc.perform(MockMvcRequestBuilders.delete("/eventgroup/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());

    }
*/
}
