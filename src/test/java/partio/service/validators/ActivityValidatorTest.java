package partio.service.validators;

import java.time.LocalDate;
import java.time.LocalTime;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import partio.domain.Activity;
import partio.domain.ActivityBuffer;
import partio.domain.Event;
import partio.domain.Scout;
import partio.repository.ActivityBufferRepository;
import partio.repository.ActivityRepository;
import partio.repository.EventRepository;
import partio.repository.ScoutRepository;
import static partio.service.validators.TestHelper.*;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class ActivityValidatorTest {

    @Autowired
    private ActivityValidator validator;
    @Autowired
    private EventRepository eventRepo;
    @Autowired
    private ActivityRepository activityRepo;
    @Autowired
    private ActivityBufferRepository bufferRepo;
    @Autowired ScoutRepository scoutRepo;
    private Scout scout;

    private Event stubEvent1;
    private Event stubEvent2;
    private ActivityBuffer stubBuffer;

    private Activity stubActivity;

    private static final String SHOULD_BE_EMPTY = "there should not be errors";
    private static final String SHOULD_NOT_BE_EMPTY = "there should be at least 1 error";

    @Before
    public void SetUp() {
        scout = new Scout("mockid", null, null, "scout");
        scoutRepo.save(scout);
        this.stubEvent1 = new Event("stub", LocalDate.now().minusMonths(1), DateNowPlusAmount(0, 0, 1), LocalTime.MIN, LocalTime.MIN, "type", "information", scout);
        this.stubEvent2 = new Event("stub", LocalDate.now().minusMonths(1), DateNowPlusAmount(0, 0, 1), LocalTime.MIN, LocalTime.MIN, "type", "information", scout);
        this.stubBuffer = new ActivityBuffer();
        eventRepo.save(stubEvent1);
        eventRepo.save(stubEvent2);
        bufferRepo.save(stubBuffer);

        this.stubActivity = new Activity(stubEvent1, null, null, "mock");
    }

    @After
    public void clean() {
        activityRepo.deleteAll();
        bufferRepo.deleteAll();
        eventRepo.deleteAll();
        scoutRepo.deleteAll();
    }

    //new activity
    @Test
    public void validTestInEvent() {
        Assert.assertTrue(SHOULD_BE_EMPTY, 0 == validator.validateNew(stubActivity).size());
    }

    @Test
    public void validTestInBuffer() {
        stubActivity.setBuffer(stubBuffer);
        stubActivity.setEvent(null);
        Assert.assertTrue(SHOULD_BE_EMPTY, 0 == validator.validateNew(stubActivity).size());
    }

    @Test
    public void invalidTestInEventAndInBuffer() {
        stubActivity.setBuffer(stubBuffer);
        Assert.assertTrue(SHOULD_NOT_BE_EMPTY, 0 != validator.validateNew(stubActivity).size());
    }
    @Test
    public void invalidTestNotInBufferOrNotInActivity() {
        stubActivity.setEvent(null);
        Assert.assertTrue(SHOULD_NOT_BE_EMPTY, 0 != validator.validateNew(stubActivity).size());
    }

    @Test
    public void invalidTestNoGuid() {
        stubActivity.setGuid("   ");
        Assert.assertTrue(SHOULD_NOT_BE_EMPTY, 0 != validator.validateNew(stubActivity).size());
        stubActivity.setGuid(null);
        Assert.assertTrue(SHOULD_NOT_BE_EMPTY, 0 != validator.validateNew(stubActivity).size());
    }
     @Test
    public void nullTest() {
        Assert.assertTrue(SHOULD_NOT_BE_EMPTY, 0 != validator.validateNew(null).size());
    }
    

    @Test
    public void duplicateTest() {
        activityRepo.save(stubActivity);
        this.stubActivity = new Activity(stubEvent1, null, null, "mock");
        Assert.assertTrue(SHOULD_NOT_BE_EMPTY, 0 != validator.validateNew(stubActivity).size());
    }

    //mod activity
     @Test
    public void nullTestMod() {
        activityRepo.save(stubActivity);
        Assert.assertTrue(SHOULD_NOT_BE_EMPTY, 0 != validator.validateChanges(stubActivity, null).size());
        Assert.assertTrue(SHOULD_NOT_BE_EMPTY, 0 != validator.validateChanges(null,stubActivity).size());
        Assert.assertTrue(SHOULD_NOT_BE_EMPTY, 0 != validator.validateChanges(null, null).size());
    }
    @Test
    public void validTestInEventNoChanges() {
        activityRepo.save(stubActivity);
        Assert.assertTrue(SHOULD_BE_EMPTY, 0 == validator.validateChanges(stubActivity, stubActivity).size());
    }

    @Test
    public void validMoveFromEventToEvent() {
        stubActivity.setEvent(stubEvent2);
        activityRepo.save(stubActivity);
        Activity changedActivity = new Activity(stubEvent1, null, null, "mock");
        Assert.assertTrue(SHOULD_BE_EMPTY, 0 == validator.validateChanges(stubActivity, changedActivity).size());
    }

    @Test
    public void validMoveFromEventToBuffer() {
        stubActivity.setEvent(stubEvent2);
        activityRepo.save(stubActivity);
        Activity changedActivity = new Activity(null, stubBuffer, null, "mock");
        Assert.assertTrue(SHOULD_BE_EMPTY, 0 == validator.validateChanges(stubActivity, changedActivity).size());
    }

    @Test
    public void validMoveFromBufferToEvent() {
        stubActivity.setBuffer(stubBuffer);
        activityRepo.save(stubActivity);
        Activity changedActivity = new Activity(stubEvent2, null, null, "mock");
        Assert.assertTrue(SHOULD_BE_EMPTY, 0 == validator.validateChanges(stubActivity, changedActivity).size());
    }
     @Test
    public void invalidChangeToBoth1() {
        stubActivity.setEvent(stubEvent2);
        activityRepo.save(stubActivity);
        Activity changedActivity = new Activity(stubEvent2, stubBuffer, null, "mock");
        Assert.assertTrue(SHOULD_NOT_BE_EMPTY, 0 != validator.validateChanges(stubActivity, changedActivity).size());
    }

    @Test
    public void invalidChangeToBoth2() {
        stubActivity.setBuffer(stubBuffer);
        activityRepo.save(stubActivity);
        Activity changedActivity = new Activity(stubEvent2, stubBuffer, null, "mock");
        Assert.assertTrue(SHOULD_NOT_BE_EMPTY, 0 != validator.validateChanges(stubActivity, changedActivity).size());
    }
    @Test
    public void invalidChangeToNone() {
        stubActivity.setBuffer(stubBuffer);
        activityRepo.save(stubActivity);
        Activity changedActivity = new Activity(null, null, null, "mock");
        Assert.assertTrue(SHOULD_NOT_BE_EMPTY, 0 != validator.validateChanges(stubActivity, changedActivity).size());
    }
    @Test
    public void shoudntBeAbleToChangeGuid() {
        stubActivity.setBuffer(stubBuffer);
        activityRepo.save(stubActivity);
        Activity changedActivity = new Activity(null, stubBuffer, null, "a change");
        Assert.assertTrue(SHOULD_NOT_BE_EMPTY, 0 != validator.validateChanges(stubActivity, changedActivity).size());
    }
}
