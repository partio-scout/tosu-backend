package partio.controller;


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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

        activityRepo.deleteAll();
        bufferRepository.deleteAll();
        bufferRepository.save(buffer);
    }

    @Test
    public void statusOk() throws Exception {
        mockMvc.perform(get("/activitybuffer/1"))
                .andExpect(status().isOk());
    }
//
//    @Test
//    public void testAddActivityToBuffer() throws Exception {
//        
//        mockMvc.perform(MockMvcRequestBuilders.post("/activitybuffer/{id}/activities/", buffer.getId())
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(helper.activityToJson(activity)))
//                .andDo(print())
//                .andExpect(status().isOk());
//        
//        assertEquals(buffer, activity.getBuffer());
//    }
}
