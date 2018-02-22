package partio.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
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
import partio.domain.Event;
import partio.repository.ActivityRepository;
import partio.repository.EventRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityControllerTest {

    @Autowired
    private WebApplicationContext webAppContext;
    @Autowired
    private EventRepository eventRepo;
    @Autowired
    private ActivityRepository activityRepo;

    private MockMvc mockMvc;
    private Event event;
    private TestHelper helper;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
        event = new Event("le stub", LocalDate.now(), LocalDate.now(), LocalTime.MAX, LocalTime.MAX, "stub type", "this is a valid stub");
        helper = new TestHelper();
        
        eventRepo.deleteAll();
        activityRepo.deleteAll();
    }

    @Test
    public void statusOk() throws Exception {
        mockMvc.perform(get("/events"))
                .andExpect(status().isOk());
    }

    @Test
    public void validPost() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(helper.eventToJson(event)))
                .andExpect(status().isOk());

        Event indbEvent = eventRepo.findAll().get(0);
        Activity stub = new Activity(indbEvent, "this is a valid stub");

        mockMvc.perform(MockMvcRequestBuilders.post("/events/" + indbEvent.getId() + "/activities")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(helper.activityToJson(stub)))
                .andExpect(status().isOk());

        Activity savedindb = activityRepo.findAll().get(0);

        Assert.assertTrue(savedindb.getInformation().equals(stub.getInformation()));
        Assert.assertTrue(Objects.equals(savedindb.getEvent().getId(), indbEvent.getId()));
    }

    @Test
    public void invalidPostWithValidEvent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(helper.eventToJson(event)))
                .andExpect(status().isOk());

        Event indbEvent = eventRepo.findAll().get(0);
        Activity stub = new Activity(null, " ");

        mockMvc.perform(MockMvcRequestBuilders.post("/events/" + indbEvent.getId() + "/activities")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(helper.activityToJson(stub)))
                .andExpect(status().isBadRequest());

        Assert.assertTrue(activityRepo.findAll().isEmpty());
    }

    @Test
    public void invalidPostWithoutEvent() throws Exception {
        Activity stub = new Activity(null, "this is a stub");
        mockMvc.perform(MockMvcRequestBuilders.post("/events/" + 2 + "/activities")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(helper.activityToJson(stub)))
                .andExpect(status().isNotFound());

        Assert.assertTrue(activityRepo.findAll().isEmpty());
    }
    
    @Test
    public void validDelete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(helper.eventToJson(event)))
                .andExpect(status().isOk());

        Event indbEvent = eventRepo.findAll().get(0);
        Activity stub = new Activity(indbEvent, "this is a valid stub");

        mockMvc.perform(MockMvcRequestBuilders.post("/events/" + indbEvent.getId() + "/activities")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(helper.activityToJson(stub)))
                .andExpect(status().isOk());

        Activity savedindb = activityRepo.findAll().get(0);

        mockMvc.perform(MockMvcRequestBuilders.delete("/activities/" + savedindb.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
        Assert.assertTrue(activityRepo.findAll().isEmpty());
    }

    @Test
    public void deleteNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/activities/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
        Assert.assertTrue(activityRepo.findAll().isEmpty());
    }

}
