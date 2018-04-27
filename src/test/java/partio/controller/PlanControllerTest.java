package partio.controller;

import java.util.HashMap;
import java.util.Map;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import partio.domain.Activity;
import partio.domain.ActivityBuffer;
import partio.domain.Plan;
import partio.domain.Scout;
import partio.repository.ActivityBufferRepository;
import partio.repository.ActivityRepository;
import partio.repository.PlanRepository;
import partio.repository.ScoutRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlanControllerTest {

    @Autowired
    private WebApplicationContext webAppContext;
    @Autowired
    private ActivityRepository activityRepo;
    @Autowired
    private PlanRepository planRepo;
    private TestHelperJson helper;
    private MockMvc mockMvc;
    @Autowired ScoutRepository scoutRepo;
    @Autowired ActivityBufferRepository bufferRepo;
    private Scout scout;
    Map<String, Object> sessionAttrs;
    
    private Activity activity;
    private ActivityBuffer buffer;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
        helper = new TestHelperJson();
        
        scout = new Scout("mockid", null, null, "scout");
        scoutRepo.save(scout);
        buffer = new ActivityBuffer(null, scout);
        bufferRepo.save(buffer);
        activity = new Activity(null, buffer, null, "huehue");
        activityRepo.save(activity);
        
        
        sessionAttrs = new HashMap<>();
        sessionAttrs.put("scout", scout);
    }

    @After
    public void clean() {
        planRepo.deleteAll();
        activityRepo.deleteAll();
        bufferRepo.deleteAll();
        scoutRepo.deleteAll();
        
    }

    @Test
    public void validPost() throws Exception {
        
        Plan plan = new Plan(null, "title", "guid", "content");
        mockMvc.perform(MockMvcRequestBuilders.post("/activity/" + activity.getId() + "/plans")
                .sessionAttrs(sessionAttrs)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(helper.planToJson(plan)))
                .andExpect(status().isOk());

        Plan savedindb = planRepo.findAll().get(0);

        Assert.assertTrue(savedindb.getActivity().getId().equals(activity.getId()));
        Assert.assertTrue(savedindb.getGuid().equals(plan.getGuid()));
        Assert.assertTrue(savedindb.getTitle().equals(plan.getTitle()));
        Assert.assertTrue(savedindb.getContent().equals(plan.getContent()));
    }

    @Test
    public void invalidPost() throws Exception {
        Plan plan = new Plan(null, "title", "guid", "content");// 1st plan
        mockMvc.perform(MockMvcRequestBuilders.post("/activity/" + activity.getId() + "/plans")
                .sessionAttrs(sessionAttrs)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(helper.planToJson(plan)))
                .andExpect(status().isOk());

        plan = new Plan(null, "title", "guid", "content"); //duplicate plan
        mockMvc.perform(MockMvcRequestBuilders.post("/activity/" + activity.getId() + "/plans")
                .sessionAttrs(sessionAttrs)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(helper.planToJson(plan)))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void validDelete() throws Exception {
        Plan plan = new Plan(activity, "title", "guid", "content");
        planRepo.save(plan);

        mockMvc.perform(MockMvcRequestBuilders.delete("/plans/" + plan.getId())
                .sessionAttrs(sessionAttrs)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        Assert.assertTrue(planRepo.findAll().isEmpty());
    }

    @Test
    public void invalidDelete() throws Exception {
        Plan plan = new Plan(activity, "title", "guid", "content");
        planRepo.save(plan);

        mockMvc.perform(MockMvcRequestBuilders.delete("/plans/" + (plan.getId() + 1))
                .sessionAttrs(sessionAttrs)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());

        Assert.assertTrue(planRepo.findAll().isEmpty() == false);
    }
}
