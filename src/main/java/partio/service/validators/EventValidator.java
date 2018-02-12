package partio.service.validators;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import partio.domain.Event;

/*
lengths of info
are types pre-determined? if not lengths?
english finnish validation reports?
how to handle timezones?
can events be modified or created for past? done atm so cannot
 */
@Service
public class EventValidator extends Validator<Event> {

    public static final int MIN_INFORMATION_LENGTH = 0;
    public static final int MAX_INFORMATION_LENGTH = 1000;
    public static final int MIN_TYPE_LENGTH = 2;
    public static final int MAX_TYPE_LENGTH = 64;
    public static final int MIN_TITLE_LENGTH = 2;
    public static final int MAX_TITLE_LENGTH = 64;

    @Override
    public List<String> validate(Event t) {
        List<String> errors = new ArrayList<>();

        //string length
        if (!validateStringLength(t.getInformation(), MIN_INFORMATION_LENGTH, MAX_INFORMATION_LENGTH, CAN_NULL)) {
            errors.add("information length has to be between " + MIN_INFORMATION_LENGTH + "-" + MAX_INFORMATION_LENGTH);
        }
        if (!validateStringLength(t.getType(), MIN_TYPE_LENGTH, MAX_TYPE_LENGTH, NOT_NULL)) {
            errors.add("information length has to be between " + MIN_TYPE_LENGTH + "-" + MAX_TYPE_LENGTH);
        }
        if (!validateStringLength(t.getTitle(), MIN_TITLE_LENGTH, MAX_TITLE_LENGTH, NOT_NULL)) {
            errors.add("information length has to be between " + MIN_TYPE_LENGTH + "-" + MAX_TYPE_LENGTH);
        }
        //not only space in strings 
        if (!validateStringNotOnlySpaces(t.getTitle(), NOT_NULL)) {
            errors.add("title cannot be whitespace only");
        }
        if (!validateStringNotOnlySpaces(t.getInformation(), CAN_NULL)) {
            errors.add("information cannot be whitespace only");
        }
        if (!validateStringNotOnlySpaces(t.getType(), NOT_NULL)) {
            errors.add("type cannot be whitespace only");
        }
        //times
        if (!endingAfterOrsameTimeAsStart(t)) {
            errors.add("start time cannot be before end time");
        }
        if (!startAfterCurrentTime(t)) {
            errors.add("you cannot touch events doen in past");
        }
        if (!eventMaxOneYearInLength(t)) {
            errors.add("event max duration is one year");
        }
        if (!eventStartsAndEndWithinAYearOfNow(t)) {
            errors.add("event has to start and end within a year of now");
        }

        return errors;
    }

    private boolean endingAfterOrsameTimeAsStart(Event event) {
        if (event.getStartDate().isBefore(event.getEndDate())) {
            return true;
            
        } else if (event.getStartDate().isEqual(event.getEndDate())) {
            return event.getEndTime().isBefore(event.getStartTime()) == false;
       
        } else {
            return false;
        }
    }

    private boolean eventMaxOneYearInLength(Event e) {
        //returns added value, does not change itself
        LocalDate oneYearAdded = e.getStartDate().plusYears(1);

        return e.getEndDate().isAfter(oneYearAdded) == false;
    }
    
    private boolean eventStartsAndEndWithinAYearOfNow(Event e) {
        LocalDate oneYearRemoved = e.getStartDate().minusYears(1);
        if (oneYearRemoved.isAfter(LocalDate.now())) {
            return false;
        }
        oneYearRemoved = e.getEndDate().minusYears(1);
        if (oneYearRemoved.isAfter(LocalDate.now())) {
            return false;
        }
        return true;
    }

    private boolean startAfterCurrentTime(Event t) {
        LocalDate dateNow = LocalDate.now();
        if (t.getStartDate().isAfter(dateNow)) {
            return true;

        } else if (t.getStartDate().isEqual(dateNow)) {
            LocalTime timeNow = LocalTime.now();
            return !timeNow.isBefore(t.getStartTime());

        } else {
            return false;
        }
    }
    
}
