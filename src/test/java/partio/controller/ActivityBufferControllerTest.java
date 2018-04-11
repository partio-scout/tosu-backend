package partio.controller;


import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import partio.domain.Activity;
import partio.domain.ActivityBuffer;
import partio.repository.ActivityBufferRepository;
import partio.repository.ActivityRepository;
import partio.repository.EventRepository;
import partio.service.ActivityBufferService;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityBufferControllerTest {

    @Autowired
    private WebApplicationContext webAppContext;
    @Autowired
    private ActivityBufferService bufferService; //
    @Autowired
    private EventRepository eventRepo;
    @Autowired
    private ActivityRepository activityRepo;
    @Autowired
    private ActivityBufferRepository bufferRepository;

    private MockMvc mockMvc;
    private Activity activity;
    private ActivityBuffer buffer;
    private TestHelperJson helper;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
        activity = new Activity();
        helper = new TestHelperJson();
        buffer = new ActivityBuffer();
        activity.setGuid("rwer");
        bufferRepository.save(buffer);
    }
    
    @After
    public void clean() {
        activityRepo.deleteAll();
        bufferRepository.deleteAll();
    }

    @Test
    public void statusOk() throws Exception {
        mockMvc.perform(get("/activitybuffer/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddActivityToBuffer() throws Exception {      
        mockMvc.perform(MockMvcRequestBuilders.post("/activitybuffer/{id}/activities/", buffer.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(helper.activityToJson(activity)))
                .andExpect(status().isOk());
        
        Activity savedindb = activityRepo.findAll().get(0);        
        assertEquals(buffer.getId(), savedindb.getBuffer().getId());
        buffer = bufferRepository.findAll().get(0);
        assertEquals(buffer.getActivities().get(0).getGuid(), savedindb.getGuid());
        assertEquals(null, savedindb.getEvent());
    }
    
    @Test
    public void testAddActivityToBufferFail() throws Exception {     
        activity.setGuid("");
        mockMvc.perform(MockMvcRequestBuilders.post("/activitybuffer/{id}/activities/", buffer.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(helper.activityToJson(activity)))
                .andExpect(status().isBadRequest());
        
        assertEquals(activityRepo.count(), 0);
    }

}
