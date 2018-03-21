
package partio.service.validators;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import partio.domain.Activity;
import partio.domain.Plan;
import partio.repository.ActivityRepository;
import partio.repository.PlanRepository;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class PlanValidatorTest {
    
    @Autowired
    private PlanValidator validator;
    @Autowired
    private PlanRepository planRepo;
    @Autowired
    private ActivityRepository activityRepo;
    
    private Activity stubActivity;
    private Plan stubPlan;

    private static final String SHOULD_BE_EMPTY = "there should not be errors";
    private static final String SHOULD_NOT_BE_EMPTY = "there should be at least 1 error";
    
    @Before
    public void SetUp() {
        this.stubActivity = new Activity();
        activityRepo.save(stubActivity);

        this.stubPlan = new Plan(stubActivity, "title", "guid", "content");
    }

    @After
    public void clean() {
        activityRepo.deleteAll();
        planRepo.deleteAll();
    }
    
    @Test
    public void validTestInEvent() {
        Assert.assertTrue(SHOULD_BE_EMPTY, 0 == validator.validateNew(stubPlan).size());
    }
    
    @Test
    public void nullValuesInTitleAndContentNotAllowed() {
        this.stubPlan = new Plan(stubActivity, "  ", "  ", "content");
        Assert.assertTrue(SHOULD_NOT_BE_EMPTY, 0 != validator.validateNew(stubPlan).size());
        this.stubPlan = new Plan(stubActivity, null,null, "content");
        Assert.assertTrue(SHOULD_NOT_BE_EMPTY, 0 != validator.validateNew(stubPlan).size());
    }
    
    @Test
    public void noDuplicates() {
        planRepo.save(stubPlan);
        this.stubPlan = new Plan(stubActivity, "title","guid", "content");
        Assert.assertTrue(SHOULD_NOT_BE_EMPTY, 0 != validator.validateNew(stubPlan).size());
    }
    @Test
    public void nullTests() {
        Assert.assertTrue(SHOULD_NOT_BE_EMPTY, 0 != validator.validateNew(null).size());
        Assert.assertTrue(SHOULD_NOT_BE_EMPTY, 0 != validator.validateChanges(null, stubPlan).size());
        Assert.assertTrue(SHOULD_NOT_BE_EMPTY, 0 != validator.validateChanges(stubPlan, null).size());
        Assert.assertTrue(SHOULD_NOT_BE_EMPTY, 0 != validator.validateChanges(null, null).size());
    }
    
    //changing will come later as specs will be found out
    @Test
    public void detenctsNullVariables() {
        Plan changed = new Plan(stubActivity, "  ","  ", "  ");
        Assert.assertTrue(SHOULD_NOT_BE_EMPTY, 0 != validator.validateChanges(stubPlan, changed).size());
        changed = new Plan(null, null,null,null);
        Assert.assertTrue(SHOULD_NOT_BE_EMPTY, 0 != validator.validateChanges(stubPlan, changed).size());
    }
}
