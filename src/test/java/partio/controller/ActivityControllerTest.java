package partio.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
import partio.domain.Scout;
import partio.repository.ActivityBufferRepository;
import partio.repository.ActivityRepository;
import partio.repository.EventRepository;
import partio.repository.ScoutRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityControllerTest {

    @Autowired
    private WebApplicationContext webAppContext;
    @Autowired
    private EventRepository eventRepo;
    @Autowired
    private ActivityBufferRepository bufferRepo;
    @Autowired
    private ActivityRepository activityRepo;
    @Autowired ScoutRepository scoutRepo;
    private Scout scout;
    Map<String, Object> sessionAttrs;
    private MockMvc mockMvc;
    private Event event;
    private Event event2;
    private ActivityBuffer buffer;
    private TestHelperJson helper;

    @Before
    public void setUp() {
        scout = new Scout("mockid", null, null, "scout");
        scoutRepo.save(scout);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
        event = new Event("le stub", LocalDate.now(), LocalDate.now(), LocalTime.MAX, LocalTime.MAX, "stub type", "this is a valid stub", scout);
        buffer = new ActivityBuffer(null, scout);
        event2 = new Event("le stub", LocalDate.now(), LocalDate.now(), LocalTime.MAX, LocalTime.MAX, "stub type", "this is a valid stub", scout);
        helper = new TestHelperJson();
        sessionAttrs = new HashMap<>();
        sessionAttrs.put("scout", scout);
    }

    @After
    public void clean() {
        activityRepo.deleteAll();
        eventRepo.deleteAll();
        bufferRepo.deleteAll();
        scoutRepo.deleteAll();
    }

    @Test
    public void statusOk() throws Exception {
        mockMvc.perform(get("/events")
        .sessionAttrs(sessionAttrs))
                .andExpect(status().isOk());
    }

    @Test
    public void validPost() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                .sessionAttrs(sessionAttrs)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(helper.eventToJson(event)))
                .andExpect(status().isOk());

        Event indbEvent = eventRepo.findAll().get(0);
        Activity stub = new Activity(indbEvent, "this is a valid stub");

        mockMvc.perform(MockMvcRequestBuilders.post("/events/" + indbEvent.getId() + "/activities")
                .sessionAttrs(sessionAttrs)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(helper.activityToJson(stub)))
                .andExpect(status().isOk());

        Activity savedindb = activityRepo.findAll().get(0);

        Assert.assertTrue(savedindb.getGuid().equals(stub.getGuid()));
        Assert.assertTrue(Objects.equals(savedindb.getEvent().getId(), indbEvent.getId()));
    }

    @Test
    public void invalidPostWithValidEvent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                .sessionAttrs(sessionAttrs)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(helper.eventToJson(event)))
                .andExpect(status().isOk());

        Event indbEvent = eventRepo.findAll().get(0);
        Activity stub = new Activity(null, " ");

        mockMvc.perform(MockMvcRequestBuilders.post("/events/" + indbEvent.getId() + "/activities")
                .sessionAttrs(sessionAttrs)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(helper.activityToJson(stub)))
                .andExpect(status().isBadRequest());

        Assert.assertTrue(activityRepo.findAll().isEmpty());
    }

    @Test
    public void invalidPostWithoutEvent() throws Exception {
        Activity stub = new Activity(null, "this is a stub");
        mockMvc.perform(MockMvcRequestBuilders.post("/events/" + 2 + "/activities")
                .sessionAttrs(sessionAttrs)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(helper.activityToJson(stub)))
                .andExpect(status().isNotFound());

        Assert.assertTrue(activityRepo.findAll().isEmpty());
    }

    @Test
    public void validDelete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                .sessionAttrs(sessionAttrs)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(helper.eventToJson(event)))
                .andExpect(status().isOk());

        Event indbEvent = eventRepo.findAll().get(0);
        Activity stub = new Activity(indbEvent, "this is a valid stub");

        mockMvc.perform(MockMvcRequestBuilders.post("/events/" + indbEvent.getId() + "/activities")
                .sessionAttrs(sessionAttrs)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(helper.activityToJson(stub)))
                .andExpect(status().isOk());

        Activity savedindb = activityRepo.findAll().get(0);

        mockMvc.perform(MockMvcRequestBuilders.delete("/activities/" + savedindb.getId())
                .sessionAttrs(sessionAttrs)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
        Assert.assertTrue(activityRepo.findAll().isEmpty());
    }

    @Test
    public void deleteNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/activities/1")
                .sessionAttrs(sessionAttrs)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
        Assert.assertTrue(activityRepo.findAll().isEmpty());
    }

    @Test
    public void testFromEventToBuffer() throws Exception {
        eventRepo.save(event);//save first so ids dont match
        eventRepo.save(event2);
        Activity stub = new Activity(event2, "this is a valid stub");
        activityRepo.save(stub);
        bufferRepo.save(buffer);

        mockMvc.perform(MockMvcRequestBuilders.put("/activity/" + stub.getId() + "/fromevent/" + event2.getId() + "/tobuffer/" + buffer.getId())
                .sessionAttrs(sessionAttrs)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        Activity savedStub = activityRepo.findOne(stub.getId());
        Assert.assertTrue(Objects.equals(savedStub.getBuffer().getId(), buffer.getId()));
        Assert.assertTrue(savedStub.getEvent() == null);
    }

    @Test
    public void testFromEventToOtherEvent() throws Exception {
        eventRepo.save(event);//save first so ids dont match
        eventRepo.save(event2);
        Activity stub = new Activity(event2, "this is a valid stub");
        activityRepo.save(stub);

        mockMvc.perform(MockMvcRequestBuilders.put("/activity/" + stub.getId() + "/fromevent/" + event2.getId() + "/toevent/" + event.getId())
                .sessionAttrs(sessionAttrs)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        Activity savedStub = activityRepo.findOne(stub.getId());
        Assert.assertTrue(Objects.equals(savedStub.getEvent().getId(), event.getId()));
        Assert.assertTrue(savedStub.getBuffer() == null);
    }

    @Test
    public void testFromBufferToEvent() throws Exception {
        eventRepo.save(event);//save first so ids dont match
        eventRepo.save(event2);
        bufferRepo.save(buffer);
        Activity stub = new Activity(null, buffer, null, "ayaaa");
        activityRepo.save(stub);

        mockMvc.perform(MockMvcRequestBuilders.put("/activity/" + stub.getId() + "/frombuffer/" + buffer.getId() + "/toevent/" + event2.getId())
                .sessionAttrs(sessionAttrs)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        Activity savedStub = activityRepo.findOne(stub.getId());
        Assert.assertTrue(Objects.equals(savedStub.getEvent().getId(), event2.getId()));
        Assert.assertTrue(savedStub.getBuffer() == null);
    }

}
