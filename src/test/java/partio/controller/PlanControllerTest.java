package partio.controller;

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
import partio.domain.Plan;
import partio.repository.ActivityRepository;
import partio.repository.PlanRepository;

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

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
        helper = new TestHelperJson();
    }

    @After
    public void clean() {
        planRepo.deleteAll();
        activityRepo.deleteAll();
    }

    @Test
    public void validPost() throws Exception {
        Activity activity = new Activity("huehue");
        activityRepo.save(activity);
        Plan plan = new Plan(null, "title", "guid", "content");
        System.out.println(helper.planToJson(plan));
        mockMvc.perform(MockMvcRequestBuilders.post("/activity/" + activity.getId() + "/plans")
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
        Activity activity = new Activity("huehue");
        activityRepo.save(activity);
        Plan plan = new Plan(null, "title", "guid", "content");
        mockMvc.perform(MockMvcRequestBuilders.post("/activity/" + activity.getId() + "/plans")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(helper.planToJson(plan)))
                .andExpect(status().isOk());

        plan = new Plan(null, "title", "guid", "content");
        mockMvc.perform(MockMvcRequestBuilders.post("/activity/" + activity.getId() + "/plans")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(helper.planToJson(plan)))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void validDelete() throws Exception {
        Activity activity = new Activity("huehue");
        activityRepo.save(activity);
        Plan plan = new Plan(activity, "title", "guid", "content");
        planRepo.save(plan);

        mockMvc.perform(MockMvcRequestBuilders.delete("/plans/" + plan.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        Assert.assertTrue(planRepo.findAll().isEmpty());
    }

    @Test
    public void invalidDelete() throws Exception {
        Activity activity = new Activity("huehue");
        activityRepo.save(activity);
        Plan plan = new Plan(activity, "title", "guid", "content");
        planRepo.save(plan);

        mockMvc.perform(MockMvcRequestBuilders.delete("/plans/" + (plan.getId() + 1))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());

        Assert.assertTrue(planRepo.findAll().isEmpty() == false);
    }
}
