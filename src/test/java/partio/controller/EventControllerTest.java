package partio.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import partio.domain.Event;
import partio.domain.EventGroup;
import partio.repository.EventGroupRepository;
import partio.repository.EventRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventControllerTest {

    @Autowired
    private WebApplicationContext webAppContext;

    @Autowired
    private EventRepository eventRepo;
    @Autowired
    private EventGroupRepository groupRepo;

    private MockMvc mockMvc;
    private Event validStub;
    private Event invalidStub;
    private TestHelper helper;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
        validStub = new Event("le stub", LocalDate.now(), LocalDate.now(), LocalTime.MAX, LocalTime.MAX, "stub type", "this is a valid stub");
        invalidStub = new Event("", LocalDate.now(), LocalDate.now(), LocalTime.MAX, LocalTime.MAX, " ", " ");
        helper = new TestHelper();
        eventRepo.deleteAll();
        groupRepo.deleteAll();
    }

    @Test
    public void statusOk() throws Exception {
        mockMvc.perform(get("/events"))
                .andExpect(status().isOk());
    }

    @Test
    public void validPost() throws Exception {
        ResultActions res = mockMvc.perform(MockMvcRequestBuilders.post("/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(helper.eventToJson(validStub)))
                .andExpect(status().isOk());

        String responseBody = res.andReturn().getResponse().getContentAsString();
        Assert.assertTrue(responseBody.contains(helper.responseExpectedToContain(validStub)));
    }
     @Test
    public void validWithGroupPost() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/eventgroup")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
        
        EventGroup group = groupRepo.findAll().get(0);      
        validStub.setGroupId(group);
        
        ResultActions res = mockMvc.perform(MockMvcRequestBuilders.post("/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(helper.eventToJson(validStub)))
                .andExpect(status().isOk());

         System.out.println("expexted---\n        "+helper.responseExpectedToContain(validStub)+"<-expexted");
         System.out.println("received--\n"+res.andReturn().getResponse().getContentAsString()+"<-received");
        String responseBody = res.andReturn().getResponse().getContentAsString();
        Assert.assertTrue(responseBody.contains(helper.responseExpectedToContain(validStub)));
        Assert.assertTrue(responseBody.contains("\"groupId\":" + group.getId()));
    } 

    @Test
    public void invalidPost() throws Exception {
        ResultActions res = mockMvc.perform(MockMvcRequestBuilders.post("/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(helper.eventToJson(invalidStub)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void validPut() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(helper.eventToJson(validStub)))
                .andExpect(status().isOk());

        long id = eventRepo.findAll().get(0).getId();

        validStub.setInformation("we have modded info for testing");
        ResultActions res = mockMvc.perform(MockMvcRequestBuilders.put("/events/"+ id)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(helper.eventToJson(validStub)))
                .andExpect(status().isOk());

        String responseBody = res.andReturn().getResponse().getContentAsString();
        Assert.assertTrue(responseBody.contains(helper.responseExpectedToContain(validStub)));
    }
    
    @Test
    public void invalidPut() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(helper.eventToJson(validStub)))
                .andExpect(status().isOk());

        long id = eventRepo.findAll().get(0).getId();

        validStub.setInformation(" ");
        ResultActions res = mockMvc.perform(MockMvcRequestBuilders.put("/events/"+ id)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(helper.eventToJson(validStub)))
                .andExpect(status().isBadRequest());

        String responseBody = res.andReturn().getResponse().getContentAsString();
        Assert.assertFalse(responseBody.contains(helper.responseExpectedToContain(validStub)));
    }
    
    @Test
    public void validDelete() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders.post("/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(helper.eventToJson(validStub)))
                .andExpect(status().isOk());
        
        long id = eventRepo.findAll().get(0).getId();
        ResultActions res = mockMvc.perform(MockMvcRequestBuilders.delete("/events/"+ id)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        Assert.assertTrue(eventRepo.findOne(id) == null);
    }
    
    @Test
    public void invalidDelete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/events/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(helper.eventToJson(validStub)))
                .andExpect(status().isNotFound());
    }
}
