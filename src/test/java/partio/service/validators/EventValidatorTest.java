package partio.service.validators;

import java.time.LocalDate;
import java.time.LocalTime;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import partio.domain.Event;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class EventValidatorTest {

    @Autowired
    private EventValidator validator;
    private Event preEvent;

    @Before
    public void makePreEvent() {
        this.preEvent = new Event("stub", LocalDate.now().minusMonths(1), DateNowPlusAmount(0, 0, 1), LocalTime.MIN, LocalTime.MIN, "type", "information", null);
    }

//helpers
    public LocalDate DateNowPlusAmount(int years, int months, int days) {
        LocalDate date = LocalDate.now();
        date = date.plusDays(days);
        date = date.plusMonths(months);
        date = date.plusYears(years);
        return date;
    }

    // watch out for goin over midnight
    public LocalTime TimeNowPlusAmount(int hours, int minutes) {
        LocalTime time = LocalTime.now();
        time = time.plusMinutes(minutes);
        time = time.plusHours(hours);
        return time;
    }

    public String makeStringLengthOf(int length, char toAppend) {
        StringBuilder sb = new StringBuilder(Math.max(length, 0));
        for (int i = 0; i < length; i++) {
            sb.append(toAppend);
        }
        return sb.toString();
    }

    //TEST NEW
    //date valid tests
    @Test
    public void validNewEventStart1YearMax() {
        Event stub = new Event("lol", DateNowPlusAmount(1, 0, 0), DateNowPlusAmount(1, 0, 0), LocalTime.MIN, LocalTime.MIN, "ass", "asshole", null);
        Assert.assertTrue(validator.validateNew(stub).toString(), 0 == validator.validateNew(stub).size());
    }

    @Test
    public void validNewEventStartIsNowMin() {
        Event stub = new Event("lol", DateNowPlusAmount(0, 0, 0), DateNowPlusAmount(0, 0, 0), LocalTime.now(), LocalTime.now(), "ass", "asshole", null);
        Assert.assertTrue(validator.validateNew(stub).toString(), 0 == validator.validateNew(stub).size());
    }

    @Test
    public void validNewEventEndsInYear() {
        Event stub = new Event("lol", DateNowPlusAmount(0, 0, 0), DateNowPlusAmount(1, 0, 0), LocalTime.MAX, LocalTime.MAX, "ass", "asshole", null);
        Assert.assertTrue(validator.validateNew(stub).toString(), 0 == validator.validateNew(stub).size());
    }

    @Test
    public void validNewEventAcceptedMonth() {
        Event stub = new Event("lol", DateNowPlusAmount(0, 1, 2), DateNowPlusAmount(0, 2, 2), LocalTime.MIN, LocalTime.MIN, "ass", "asshole", null);
        Assert.assertTrue(validator.validateNew(stub).toString(), 0 == validator.validateNew(stub).size());
    }

    @Test
    public void validNewEventAcceptedDay() {
        Event stub = new Event("lol", DateNowPlusAmount(0, 2, 1), DateNowPlusAmount(0, 2, 2), LocalTime.MIN, LocalTime.MIN, "ass", "asshole", null);
        Assert.assertTrue(validator.validateNew(stub).toString(), 0 == validator.validateNew(stub).size());
    }

    @Test
    public void validNewEventAcceptedMaxLength() {
        Event stub = new Event("lol", DateNowPlusAmount(0, 0, 0), DateNowPlusAmount(0, 12, 0), LocalTime.MAX, LocalTime.MAX, "ass", "asshole", null);
        Assert.assertTrue(validator.validateNew(stub).toString(), 0 == validator.validateNew(stub).size());
    }

//date invalid tests
    @Test
    public void invalidNewEventStartOver1Year() {
        Event stub = new Event("lol", DateNowPlusAmount(1, 0, 1), DateNowPlusAmount(1, 0, 1), LocalTime.MIN, LocalTime.MIN, "ass", "asshole", null);
        Assert.assertFalse("validator should have found errors!", 0 == validator.validateNew(stub).size());
    }

    @Test
    public void invalidNewEventInPast() {
        Event stub = new Event("lol", LocalDate.now().minusDays(1), DateNowPlusAmount(0, 0, 1), LocalTime.MIN, LocalTime.MIN, "ass", "asshole", null);
        Assert.assertFalse("validator should have found errors!", 0 == validator.validateNew(stub).size());
    }

    @Test
    public void invalidNewEventEndsInOverAYearButValidLength() {
        Event stub = new Event("lol", DateNowPlusAmount(0, 1, 0), DateNowPlusAmount(1, 0, 1), LocalTime.MIN, LocalTime.MIN, "ass", "asshole", null);
        Assert.assertFalse("validator should have found errors!", 0 == validator.validateNew(stub).size());
    }

    @Test
    public void invalidNewEventAcceptedMonth() {
        Event stub = new Event("lol", DateNowPlusAmount(0, 2, 2), DateNowPlusAmount(0, 1, 2), LocalTime.MIN, LocalTime.MIN, "ass", "asshole", null);
        Assert.assertFalse("validator should have found errors!", 0 == validator.validateNew(stub).size());
    }

    @Test
    public void invalidNewEventAcceptedDay() {
        Event stub = new Event("lol", DateNowPlusAmount(0, 2, 2), DateNowPlusAmount(0, 2, 1), LocalTime.MIN, LocalTime.MIN, "ass", "asshole", null);
        Assert.assertFalse("validator should have found errors!", 0 == validator.validateNew(stub).size());
    }

    @Test
    public void invalidNewEventAcceptedMaxLength() {
        Event stub = new Event("lol", DateNowPlusAmount(0, 0, 0), DateNowPlusAmount(0, 12, 1), LocalTime.MIN, LocalTime.MIN, "ass", "asshole", null);
        Assert.assertFalse("validator should have found errors!", 0 == validator.validateNew(stub).size());
    }

//time valid test
    @Test
    public void validNewEventAcceptedSameAsCurrentTime() {
        Event stub = new Event("lol", LocalDate.now(), LocalDate.now(), LocalTime.now(), LocalTime.now(), "ass", "asshole", null);
        Assert.assertTrue(validator.validateNew(stub).toString(), 0 == validator.validateNew(stub).size());
    }

    @Test
    public void validNewEventAcceptedTimeDifferenceMax() {
        Event stub = new Event("lol", LocalDate.now().plusDays(1), LocalDate.now().plusDays(1), LocalTime.MIN, LocalTime.MAX, "ass", "asshole", null);
        Assert.assertTrue(validator.validateNew(stub).toString(), 0 == validator.validateNew(stub).size());
    }

//time invalid tests
    @Test
    public void invalidNewEventAcceptedAfterBeforeStartTimeMin() {
        LocalTime beforeNoon = LocalTime.NOON.minusMinutes(1);
        Event stub = new Event("lol", LocalDate.now(), LocalDate.now(), LocalTime.NOON, beforeNoon, "ass", "asshole", null);
        Assert.assertFalse("validator should have found errors!", 0 == validator.validateNew(stub).size());
    }

    @Test
    public void invalidNewEventAcceptedAfterBeforeStartTimeHour() {
        LocalTime beforeNoon = LocalTime.NOON.minusHours(1);
        Event stub = new Event("lol", LocalDate.now(), LocalDate.now(), LocalTime.NOON, beforeNoon, "ass", "asshole", null);
        Assert.assertFalse("validator should have found errors!", 0 == validator.validateNew(stub).size());
    }

//String valid tests
    @Test
    public void validNewEventAcceptedMinStringLengths() {
        Event stub = new Event(makeStringLengthOf(EventValidator.MIN_TITLE_LENGTH, 'a'), DateNowPlusAmount(0, 2, 1),
                DateNowPlusAmount(0, 2, 2), LocalTime.MIN, LocalTime.MIN,
                makeStringLengthOf(EventValidator.MIN_TYPE_LENGTH, 'a'),
                makeStringLengthOf(EventValidator.MIN_INFORMATION_LENGTH, 'a'), null);
        Assert.assertTrue(validator.validateNew(stub).toString(), 0 == validator.validateNew(stub).size());
    }

    @Test
    public void validNewEventAcceptedMaxStringLengths() {
        Event stub = new Event(makeStringLengthOf(EventValidator.MAX_TITLE_LENGTH, 'a'), DateNowPlusAmount(0, 2, 1),
                DateNowPlusAmount(0, 2, 2), LocalTime.MIN, LocalTime.MIN,
                makeStringLengthOf(EventValidator.MAX_TYPE_LENGTH, 'a'),
                makeStringLengthOf(EventValidator.MAX_INFORMATION_LENGTH, 'a'), null);
        Assert.assertTrue(validator.validateNew(stub).toString(), 0 == validator.validateNew(stub).size());
    }

//String invalid tests
    @Test
    public void invalidNewEventAcceptedMinStringLengths() {
        //THIS TEST REQUIRES AT LEAST ONE ATTRIBUTE IN EVENTVALIDATOR TO BE MANDATORY AND OVER 0 IN LENGTH
        Event stub = new Event(makeStringLengthOf(EventValidator.MIN_TITLE_LENGTH - 1, 'a'), DateNowPlusAmount(0, 2, 1),
                DateNowPlusAmount(0, 2, 2), LocalTime.MIN, LocalTime.MIN,
                makeStringLengthOf(EventValidator.MIN_TYPE_LENGTH - 1, 'a'),
                makeStringLengthOf(EventValidator.MIN_INFORMATION_LENGTH - 1, 'a'), null);
        Assert.assertFalse("validator should have found errors!", 0 == validator.validateNew(stub).size());
    }

    @Test
    public void invalidNewEventAcceptedMaxStringLengths() {
        Event stub = new Event(makeStringLengthOf(EventValidator.MAX_TITLE_LENGTH + 1, 'a'), DateNowPlusAmount(0, 2, 1),
                DateNowPlusAmount(0, 2, 2), LocalTime.MIN, LocalTime.MIN,
                makeStringLengthOf(EventValidator.MAX_TYPE_LENGTH + 1, 'a'),
                makeStringLengthOf(EventValidator.MAX_INFORMATION_LENGTH + 1, 'a'), null);
        Assert.assertFalse("validator should have found errors!", 0 == validator.validateNew(stub).size());
    }

    @Test
    public void invalidNewEventAcceptedStringWhiteSpace() {
        //THIS TEST REQUIRES AT LEAST ONE ATTRIBUTE IN EVENTVALIDATOR TO BE MANDATORY AND CONFIGURED NOT TO BE WHITESPACE
        Event stub = new Event(makeStringLengthOf(EventValidator.MAX_TITLE_LENGTH + 1, ' '), DateNowPlusAmount(0, 2, 1),
                DateNowPlusAmount(0, 2, 2), LocalTime.MIN, LocalTime.MIN,
                makeStringLengthOf(EventValidator.MAX_TYPE_LENGTH + 1, ' '),
                makeStringLengthOf(EventValidator.MAX_INFORMATION_LENGTH + 1, ' '), null);
        Assert.assertFalse("validator should have found errors!", 0 == validator.validateNew(stub).size());
    }

    @Test
    public void invalidNewEventAcceptedStringWhiteSpace2() {
        //THIS TEST REQUIRES AT LEAST ONE ATTRIBUTE IN EVENTVALIDATOR TO BE MANDATORY AND CONFIGURED NOT TO BE WHITESPACE

        Event stub = new Event(makeStringLengthOf(EventValidator.MAX_TITLE_LENGTH + 1, '\n'), DateNowPlusAmount(0, 2, 1),
                DateNowPlusAmount(0, 2, 2), LocalTime.MIN, LocalTime.MIN,
                makeStringLengthOf(EventValidator.MAX_TYPE_LENGTH + 1, '\n'),
                makeStringLengthOf(EventValidator.MAX_INFORMATION_LENGTH + 1, '\n'), null);
        Assert.assertFalse("validator should have found errors!", 0 == validator.validateNew(stub).size());
    }

    //TEST MOD
    //SAME AS CREATE BUT AT THE END TESTED MODDING EVENTS THAT HAVE BEGUN
    //date valid tests
    @Test
    public void validModEventStart1YearMax() {
        Event stub = new Event("lol", DateNowPlusAmount(1, 0, 0), DateNowPlusAmount(1, 0, 0), LocalTime.MIN, LocalTime.MIN, "ass", "asshole", null);
        Assert.assertTrue(validator.validateChanges(preEvent, stub).toString(), 0 == validator.validateChanges(preEvent, stub).size());
    }

    @Test
    public void validModEventStartIsNowMin() {
        Event stub = new Event("lol", DateNowPlusAmount(0, 0, 0), DateNowPlusAmount(0, 0, 0), LocalTime.MIN, LocalTime.MIN, "ass", "asshole", null);
        Assert.assertTrue(validator.validateChanges(preEvent, stub).toString(), 0 == validator.validateChanges(preEvent, stub).size());
    }

    @Test
    public void validModEventEndsInYear() {
        Event stub = new Event("lol", DateNowPlusAmount(0, 0, 0), DateNowPlusAmount(1, 0, 0), LocalTime.MIN, LocalTime.MIN, "ass", "asshole", null);
        Assert.assertTrue(validator.validateChanges(preEvent, stub).toString(), 0 == validator.validateChanges(preEvent, stub).size());
    }

    @Test
    public void validModEventAcceptedMonth() {
        Event stub = new Event("lol", DateNowPlusAmount(0, 1, 2), DateNowPlusAmount(0, 2, 2), LocalTime.MIN, LocalTime.MIN, "ass", "asshole", null);
        Assert.assertTrue(validator.validateChanges(preEvent, stub).toString(), 0 == validator.validateChanges(preEvent, stub).size());
    }

    @Test
    public void validModEventAcceptedDay() {
        Event stub = new Event("lol", DateNowPlusAmount(0, 2, 1), DateNowPlusAmount(0, 2, 2), LocalTime.MIN, LocalTime.MIN, "ass", "asshole", null);
        Assert.assertTrue(validator.validateChanges(preEvent, stub).toString(), 0 == validator.validateChanges(preEvent, stub).size());
    }

    @Test
    public void validModEventAcceptedMaxLength() {
        Event stub = new Event("lol", DateNowPlusAmount(0, 0, 0), DateNowPlusAmount(0, 12, 0), LocalTime.MIN, LocalTime.MIN, "ass", "asshole", null);
        Assert.assertTrue(validator.validateChanges(preEvent, stub).toString(), 0 == validator.validateChanges(preEvent, stub).size());
    }

//date invalid tests
    @Test
    public void invalidModEventStartOver1Year() {
        Event stub = new Event("lol", DateNowPlusAmount(1, 0, 1), DateNowPlusAmount(1, 0, 1), LocalTime.MIN, LocalTime.MIN, "ass", "asshole", null);
        Assert.assertFalse(validator.validateNew(stub).toString(), 0 == validator.validateNew(stub).size());
    }

    @Test
    public void invalidModEventInPast() {
        Event stub = new Event("lol", LocalDate.now().minusDays(1), DateNowPlusAmount(0, 0, 1), LocalTime.MIN, LocalTime.MIN, "ass", "asshole", null);
        Assert.assertFalse(validator.validateNew(stub).toString(), 0 == validator.validateNew(stub).size());
    }

    @Test
    public void invalidModEventEndsInOverAYearButValidLength() {
        Event stub = new Event("lol", DateNowPlusAmount(0, 1, 0), DateNowPlusAmount(1, 0, 1), LocalTime.MIN, LocalTime.MIN, "ass", "asshole", null);
        Assert.assertFalse(validator.validateNew(stub).toString(), 0 == validator.validateNew(stub).size());
    }

    @Test
    public void invalidModEventAcceptedMonth() {
        Event stub = new Event("lol", DateNowPlusAmount(0, 2, 2), DateNowPlusAmount(0, 1, 2), LocalTime.MIN, LocalTime.MIN, "ass", "asshole", null);
        Assert.assertFalse(validator.validateNew(stub).toString(), 0 == validator.validateNew(stub).size());
    }

    @Test
    public void invalidModEventAcceptedDay() {
        Event stub = new Event("lol", DateNowPlusAmount(0, 2, 2), DateNowPlusAmount(0, 2, 1), LocalTime.MIN, LocalTime.MIN, "ass", "asshole", null);
        Assert.assertFalse(validator.validateNew(stub).toString(), 0 == validator.validateNew(stub).size());
    }

    @Test
    public void invalidModEventAcceptedMaxLength() {
        Event stub = new Event("lol", DateNowPlusAmount(0, 0, 0), DateNowPlusAmount(0, 12, 1), LocalTime.MIN, LocalTime.MIN, "ass", "asshole", null);
        Assert.assertFalse(validator.validateNew(stub).toString(), 0 == validator.validateNew(stub).size());
    }

//time valid test
    @Test
    public void validEventModAcceptedSameAsCurrentTime() {
        preEvent.setStartTime(LocalTime.NOON);
        preEvent.setEndTime(LocalTime.NOON);
        Event stub = new Event("lol", LocalDate.now(), LocalDate.now(), LocalTime.NOON, LocalTime.NOON, "ass", "asshole", null);
        Assert.assertTrue(validator.validateChanges(preEvent, stub).toString(), 0 == validator.validateChanges(preEvent, stub).size());
    }

    @Test
    public void validEventModAcceptedTimeDifferenceMax() {
        Event stub = new Event("lol", LocalDate.now(), LocalDate.now(), LocalTime.MIN, LocalTime.MAX, "ass", "asshole", null);
        Assert.assertTrue(validator.validateChanges(preEvent, stub).toString(), 0 == validator.validateChanges(preEvent, stub).size());
    }

//time invalid tests
    @Test
    public void invalidEventModAcceptedAfterBeforeStartTimeMin() {
        LocalTime beforeNoon = LocalTime.NOON.minusMinutes(1);
        Event stub = new Event("lol", LocalDate.now(), LocalDate.now(), LocalTime.NOON, beforeNoon, "ass", "asshole", null);
        Assert.assertFalse(validator.validateNew(stub).toString(), 0 == validator.validateNew(stub).size());
    }

    @Test
    public void invalidModEventAcceptedAfterBeforeStartTimeHour() {
        LocalTime beforeNoon = LocalTime.NOON.minusHours(1);
        Event stub = new Event("lol", LocalDate.now(), LocalDate.now(), LocalTime.NOON, beforeNoon, "ass", "asshole", null);
        Assert.assertFalse(validator.validateNew(stub).toString(), 0 == validator.validateNew(stub).size());
    }

//String valid tests
    @Test
    public void validModEventAcceptedMinStringLengths() {
        Event stub = new Event(makeStringLengthOf(EventValidator.MIN_TITLE_LENGTH, 'a'), DateNowPlusAmount(0, 2, 1),
                DateNowPlusAmount(0, 2, 2), LocalTime.MIN, LocalTime.MIN,
                makeStringLengthOf(EventValidator.MIN_TYPE_LENGTH, 'a'),
                makeStringLengthOf(EventValidator.MIN_INFORMATION_LENGTH, 'a'), null);
        Assert.assertTrue(validator.validateChanges(preEvent, stub).toString(), 0 == validator.validateChanges(preEvent, stub).size());
    }

    @Test
    public void validModEventAcceptedMaxStringLengths() {
        Event stub = new Event(makeStringLengthOf(EventValidator.MAX_TITLE_LENGTH, 'a'), DateNowPlusAmount(0, 2, 1),
                DateNowPlusAmount(0, 2, 2), LocalTime.MIN, LocalTime.MIN,
                makeStringLengthOf(EventValidator.MAX_TYPE_LENGTH, 'a'),
                makeStringLengthOf(EventValidator.MAX_INFORMATION_LENGTH, 'a'), null);
        Assert.assertTrue(validator.validateChanges(preEvent, stub).toString(), 0 == validator.validateChanges(preEvent, stub).size());
    }

//String invalid tests
    @Test
    public void invalidModEventAcceptedMinStringLengths() {
        //THIS TEST REQUIRES AT LEAST ONE ATTRIBUTE IN EVENTVALIDATOR TO BE MANDATORY AND OVER 0 IN LENGTH
        Event stub = new Event(makeStringLengthOf(EventValidator.MIN_TITLE_LENGTH - 1, 'a'), DateNowPlusAmount(0, 2, 1),
                DateNowPlusAmount(0, 2, 2), LocalTime.MIN, LocalTime.MIN,
                makeStringLengthOf(EventValidator.MIN_TYPE_LENGTH - 1, 'a'),
                makeStringLengthOf(EventValidator.MIN_INFORMATION_LENGTH - 1, 'a'), null);
        Assert.assertFalse(validator.validateNew(stub).toString(), 0 == validator.validateNew(stub).size());
    }

    @Test
    public void invalidModEventAcceptedMaxStringLengths() {
        Event stub = new Event(makeStringLengthOf(EventValidator.MAX_TITLE_LENGTH + 1, 'a'), DateNowPlusAmount(0, 2, 1),
                DateNowPlusAmount(0, 2, 2), LocalTime.MIN, LocalTime.MIN,
                makeStringLengthOf(EventValidator.MAX_TYPE_LENGTH + 1, 'a'),
                makeStringLengthOf(EventValidator.MAX_INFORMATION_LENGTH + 1, 'a'), null);
        Assert.assertFalse(validator.validateNew(stub).toString(), 0 == validator.validateNew(stub).size());
    }

    @Test
    public void invalidModEventAcceptedStringWhiteSpace() {
        //THIS TEST REQUIRES AT LEAST ONE ATTRIBUTE IN EVENTVALIDATOR TO BE MANDATORY AND CONFIGURED NOT TO BE WHITESPACE

        Event stub = new Event(makeStringLengthOf(EventValidator.MAX_TITLE_LENGTH + 1, ' '), DateNowPlusAmount(0, 2, 1),
                DateNowPlusAmount(0, 2, 2), LocalTime.MIN, LocalTime.MIN,
                makeStringLengthOf(EventValidator.MAX_TYPE_LENGTH + 1, ' '),
                makeStringLengthOf(EventValidator.MAX_INFORMATION_LENGTH + 1, ' '), null);
        Assert.assertFalse(validator.validateNew(stub).toString(), 0 == validator.validateNew(stub).size());
    }

    @Test
    public void invalidModEventAcceptedStringWhiteSpace2() {
        //THIS TEST REQUIRES AT LEAST ONE ATTRIBUTE IN EVENTVALIDATOR TO BE MANDATORY AND CONFIGURED NOT TO BE WHITESPACE
        Event stub = new Event(makeStringLengthOf(EventValidator.MAX_TITLE_LENGTH + 1, '\n'), DateNowPlusAmount(0, 2, 1),
                DateNowPlusAmount(0, 2, 2), LocalTime.MIN, LocalTime.MIN,
                makeStringLengthOf(EventValidator.MAX_TYPE_LENGTH + 1, '\n'),
                makeStringLengthOf(EventValidator.MAX_INFORMATION_LENGTH + 1, '\n'), null);
        Assert.assertFalse(validator.validateNew(stub).toString(), 0 == validator.validateNew(stub).size());
    }

    //ADDITIONAL TEST FOR EVENTS CREATED IN PAST
    @Test
    public void validModEventHasBegunNotEnded() {
        Event stub = new Event("lol", LocalDate.now().minusMonths(1), DateNowPlusAmount(0, 0, 2), LocalTime.MIN, LocalTime.MIN, "ass", "asshole", null);
        Assert.assertTrue(validator.validateChanges(preEvent, stub).toString(), 0 == validator.validateChanges(preEvent, stub).size());
    }

    @Test
    public void invalidModEventHasBegunAndEndedDate() {
        Event preStub = new Event("lol", LocalDate.now().minusDays(1), LocalDate.now().minusDays(1), LocalTime.MAX, LocalTime.MAX, "ass", "asshole", null);
        Event stub = new Event("lol", LocalDate.now().minusMonths(1), LocalDate.now().plusDays(1), LocalTime.MAX, LocalTime.MAX, "ass", "asshole", null);
        Assert.assertFalse(validator.validateChanges(preStub, stub).toString(), 0 == validator.validateChanges(preStub, stub).size());
    }

    @Test
    public void invalidModEventHasBegunAndEndedTime() {
        Event preStub = new Event("lol", LocalDate.now(), LocalDate.now(), LocalTime.MIN, LocalTime.MIN, "ass", "asshole", null);
        Event stub = new Event("lol", LocalDate.now(), DateNowPlusAmount(0, 0, 2), LocalTime.MIN, LocalTime.MIN, "ass", "asshole", null);
        Assert.assertFalse(validator.validateChanges(preStub, stub).toString(), 0 == validator.validateChanges(preStub, stub).size());
    }
}
