package partio.service.validators;

import java.time.LocalDate;
import java.time.LocalTime;
import junit.framework.Assert;
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

    
    //date valid tests
    @Test
    public void validEventStart1YearMax() {
        Event stub = new Event("lol", DateNowPlusAmount(1, 0, 0), DateNowPlusAmount(1, 0, 0), LocalTime.MIN, LocalTime.MIN, "ass", "asshole", null);
        Assert.assertTrue(validator.validate(stub).toString(), 0 == validator.validate(stub).size());
    }

    @Test
    public void validEventStartIsNowMin() {
        Event stub = new Event("lol", DateNowPlusAmount(0, 0, 0), DateNowPlusAmount(0, 0, 0), LocalTime.MIN, LocalTime.MIN, "ass", "asshole", null);
        Assert.assertTrue(validator.validate(stub).toString(), 0 == validator.validate(stub).size());
    }

    @Test
    public void validEventEndsInYear() {
        Event stub = new Event("lol", DateNowPlusAmount(0, 0, 0), DateNowPlusAmount(1, 0, 0), LocalTime.MIN, LocalTime.MIN, "ass", "asshole", null);
        Assert.assertTrue(validator.validate(stub).toString(), 0 == validator.validate(stub).size());
    }

    @Test
    public void validEventAcceptedMonth() {
        Event stub = new Event("lol", DateNowPlusAmount(0, 1, 2), DateNowPlusAmount(0, 2, 2), LocalTime.MIN, LocalTime.MIN, "ass", "asshole", null);
        Assert.assertTrue(validator.validate(stub).toString(), 0 == validator.validate(stub).size());
    }

    @Test
    public void validEventAcceptedDay() {
        Event stub = new Event("lol", DateNowPlusAmount(0, 2, 1), DateNowPlusAmount(0, 2, 2), LocalTime.MIN, LocalTime.MIN, "ass", "asshole", null);
        Assert.assertTrue(validator.validate(stub).toString(), 0 == validator.validate(stub).size());
    }

    @Test
    public void validEventAcceptedMaxLength() {
        Event stub = new Event("lol", DateNowPlusAmount(0, 0, 0), DateNowPlusAmount(0, 12, 0), LocalTime.MIN, LocalTime.MIN, "ass", "asshole", null);
        Assert.assertTrue(validator.validate(stub).toString(), 0 == validator.validate(stub).size());
    }

    
//date invalid tests
    @Test
    public void invalidEventStartOver1Year() {
        Event stub = new Event("lol", DateNowPlusAmount(1, 0, 1), DateNowPlusAmount(1, 0, 1), LocalTime.MIN, LocalTime.MIN, "ass", "asshole", null);
        Assert.assertFalse(validator.validate(stub).toString(), 0 == validator.validate(stub).size());
    }

    @Test
    public void invalidEventInPast() {
        Event stub = new Event("lol", LocalDate.now().minusDays(1), DateNowPlusAmount(0, 0, 1), LocalTime.MIN, LocalTime.MIN, "ass", "asshole", null);
        Assert.assertFalse(validator.validate(stub).toString(), 0 == validator.validate(stub).size());
    }

    @Test
    public void invalidEventEndsInOverAYearButValidLength() {
        Event stub = new Event("lol", DateNowPlusAmount(0, 1, 0), DateNowPlusAmount(1, 0, 1), LocalTime.MIN, LocalTime.MIN, "ass", "asshole", null);
        Assert.assertFalse(validator.validate(stub).toString(), 0 == validator.validate(stub).size());
    }

    @Test
    public void invalidEventAcceptedMonth() {
        Event stub = new Event("lol", DateNowPlusAmount(0, 2, 2), DateNowPlusAmount(0, 1, 2), LocalTime.MIN, LocalTime.MIN, "ass", "asshole", null);
        Assert.assertFalse(validator.validate(stub).toString(), 0 == validator.validate(stub).size());
    }

    @Test
    public void invalidEventAcceptedDay() {
        Event stub = new Event("lol", DateNowPlusAmount(0, 2, 2), DateNowPlusAmount(0, 2, 1), LocalTime.MIN, LocalTime.MIN, "ass", "asshole", null);
        Assert.assertFalse(validator.validate(stub).toString(), 0 == validator.validate(stub).size());
    }

    @Test
    public void invalidEventAcceptedMaxLength() {
        Event stub = new Event("lol", DateNowPlusAmount(0, 0, 0), DateNowPlusAmount(0, 12, 1), LocalTime.MIN, LocalTime.MIN, "ass", "asshole", null);
        Assert.assertFalse(validator.validate(stub).toString(), 0 == validator.validate(stub).size());
    }

    
//time valid test
    @Test
    public void validEventAcceptedSameAsCurrentTime() {
        Event stub = new Event("lol", LocalDate.now(), LocalDate.now(), LocalTime.now(), LocalTime.now(), "ass", "asshole", null);
        Assert.assertTrue(validator.validate(stub).toString(), 0 == validator.validate(stub).size());
    }

    @Test
    public void validEventAcceptedTimeDifferenceMax() {
        Event stub = new Event("lol", LocalDate.now(), LocalDate.now(), LocalTime.MIN, LocalTime.MAX, "ass", "asshole", null);
        Assert.assertTrue(validator.validate(stub).toString(), 0 == validator.validate(stub).size());
    }

    
//time invalid tests
    @Test
    public void invalidEventAcceptedAfterBeforeStartTimeMin() {
        LocalTime beforeNoon = LocalTime.NOON.minusMinutes(1);
        Event stub = new Event("lol", LocalDate.now(), LocalDate.now(), LocalTime.NOON, beforeNoon, "ass", "asshole", null);
        Assert.assertFalse(validator.validate(stub).toString(), 0 == validator.validate(stub).size());
    }

    @Test
    public void invalidEventAcceptedAfterBeforeStartTimeHour() {
        LocalTime beforeNoon = LocalTime.NOON.minusHours(1);
        Event stub = new Event("lol", LocalDate.now(), LocalDate.now(), LocalTime.NOON, beforeNoon, "ass", "asshole", null);
        Assert.assertFalse(validator.validate(stub).toString(), 0 == validator.validate(stub).size());
    }

  
//String valid tests
    @Test
    public void validEventAcceptedMinStringLengths() {
        Event stub = new Event(makeStringLengthOf(EventValidator.MIN_TITLE_LENGTH, 'a'), DateNowPlusAmount(0, 2, 1),
                DateNowPlusAmount(0, 2, 2), LocalTime.MIN, LocalTime.MIN,
                makeStringLengthOf(EventValidator.MIN_TYPE_LENGTH, 'a'),
                makeStringLengthOf(EventValidator.MIN_INFORMATION_LENGTH, 'a'), null);
        Assert.assertTrue(validator.validate(stub).toString(), 0 == validator.validate(stub).size());
    }

    @Test
    public void validEventAcceptedMaxStringLengths() {
        Event stub = new Event(makeStringLengthOf(EventValidator.MAX_TITLE_LENGTH, 'a'), DateNowPlusAmount(0, 2, 1),
                DateNowPlusAmount(0, 2, 2), LocalTime.MIN, LocalTime.MIN,
                makeStringLengthOf(EventValidator.MAX_TYPE_LENGTH, 'a'),
                makeStringLengthOf(EventValidator.MAX_INFORMATION_LENGTH, 'a'), null);
        Assert.assertTrue(validator.validate(stub).toString(), 0 == validator.validate(stub).size());
    }


//String invalid tests
    @Test
    public void invalidEventAcceptedMinStringLengths() {
        /*THIS TEST REQUIRES AT LEAST ONE ATTRIBUTE IN EVENTVALIDATOR TO BE MANDATORY AND OVER 0 IN LENGTH*/
        Event stub = new Event(makeStringLengthOf(EventValidator.MIN_TITLE_LENGTH - 1, 'a'), DateNowPlusAmount(0, 2, 1),
                DateNowPlusAmount(0, 2, 2), LocalTime.MIN, LocalTime.MIN,
                makeStringLengthOf(EventValidator.MIN_TYPE_LENGTH - 1, 'a'),
                makeStringLengthOf(EventValidator.MIN_INFORMATION_LENGTH - 1, 'a'), null);
        Assert.assertFalse(validator.validate(stub).toString(), 0 == validator.validate(stub).size());
    }

    @Test
    public void invalidEventAcceptedMaxStringLengths() {
        Event stub = new Event(makeStringLengthOf(EventValidator.MAX_TITLE_LENGTH + 1, 'a'), DateNowPlusAmount(0, 2, 1),
                DateNowPlusAmount(0, 2, 2), LocalTime.MIN, LocalTime.MIN,
                makeStringLengthOf(EventValidator.MAX_TYPE_LENGTH + 1, 'a'),
                makeStringLengthOf(EventValidator.MAX_INFORMATION_LENGTH + 1, 'a'), null);
        Assert.assertFalse(validator.validate(stub).toString(), 0 == validator.validate(stub).size());
    }

    @Test
    public void invalidEventAcceptedStringWhiteSpace() {
        /*THIS TEST REQUIRES AT LEAST ONE ATTRIBUTE IN EVENTVALIDATOR TO BE MANDATORY AND CONFIGURED NOT TO BE WHITESPACE*/

        Event stub = new Event(makeStringLengthOf(EventValidator.MAX_TITLE_LENGTH + 1, ' '), DateNowPlusAmount(0, 2, 1),
                DateNowPlusAmount(0, 2, 2), LocalTime.MIN, LocalTime.MIN,
                makeStringLengthOf(EventValidator.MAX_TYPE_LENGTH + 1, ' '),
                makeStringLengthOf(EventValidator.MAX_INFORMATION_LENGTH + 1, ' '), null);
        Assert.assertFalse(validator.validate(stub).toString(), 0 == validator.validate(stub).size());
    }

    @Test
    public void invalidEventAcceptedStringWhiteSpace2() {
        /*THIS TEST REQUIRES AT LEAST ONE ATTRIBUTE IN EVENTVALIDATOR TO BE MANDATORY AND CONFIGURED NOT TO BE WHITESPACE*/

        Event stub = new Event(makeStringLengthOf(EventValidator.MAX_TITLE_LENGTH + 1, '\n'), DateNowPlusAmount(0, 2, 1),
                DateNowPlusAmount(0, 2, 2), LocalTime.MIN, LocalTime.MIN,
                makeStringLengthOf(EventValidator.MAX_TYPE_LENGTH + 1, '\n'),
                makeStringLengthOf(EventValidator.MAX_INFORMATION_LENGTH + 1, '\n'), null);
        Assert.assertFalse(validator.validate(stub).toString(), 0 == validator.validate(stub).size());
    }
}
