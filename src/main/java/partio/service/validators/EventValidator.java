package partio.service.validators;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import partio.domain.Event;
import partio.repository.EventGroupRepository;

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

    @Autowired
    EventGroupRepository groupRepository;

    @Override
    public List<String> validateNew(Event event) {
        try {
            List<String> errors = validateNewAndOld(event);

            if (!startAfterCurrentTime(event)) {
                errors.add("you cannot create an event that starts in the past");
            }

            return errors;
        } catch (Exception e) {
            List<String> err = new ArrayList<>();
            err.add("unexpected error:" + e.toString());
            return err;
        }
    }

    /*difference made so if event has started its starttime cannot be changed*/
    @Override
    public List<String> validateChanges(Event originalEvent, Event moddedEvent) {
        try {
            List<String> errors = validateNewAndOld(moddedEvent);
            if (!startAfterCurrentTime(originalEvent)) {

                if (!originalEvent.getStartDate().isEqual(moddedEvent.getStartDate())
                        && !originalEvent.getStartTime().equals(moddedEvent.getStartTime())) {
                    errors.add("you cannot change start time of started event!");
                }

                if (originalEvent.getEndDate().isBefore(LocalDate.now())) {
                    errors.add("you cannot modify ended event!");

                } else if (originalEvent.getEndDate().isEqual(LocalDate.now())) {
                    if (originalEvent.getEndTime().isBefore(LocalTime.now())) {
                        errors.add("you cannot modify ended event!");
                    }
                }
            }
            return errors;
        } catch (Exception e) {
            List<String> err = new ArrayList<>();
            err.add("unexpected error:" + e.toString());
            return err;
        }
    }

    @Override
    protected List<String> validateNewAndOld(Event event
    ) {
        List<String> errors = new ArrayList<>();

        //string length
        if (!validateStringLength(event.getInformation(), MIN_INFORMATION_LENGTH, MAX_INFORMATION_LENGTH, CAN_NULL)) {
            errors.add("information length has to be between " + MIN_INFORMATION_LENGTH + "-" + MAX_INFORMATION_LENGTH);
        }
        if (!validateStringLength(event.getType(), MIN_TYPE_LENGTH, MAX_TYPE_LENGTH, NOT_NULL)) {
            errors.add("type length has to be between " + MIN_TYPE_LENGTH + "-" + MAX_TYPE_LENGTH);
        }
        if (!validateStringLength(event.getTitle(), MIN_TITLE_LENGTH, MAX_TITLE_LENGTH, NOT_NULL)) {
            errors.add("title length has to be between " + MIN_TYPE_LENGTH + "-" + MAX_TYPE_LENGTH);
        }
        //not only space in strings 
        if (!validateStringNotOnlySpaces(event.getTitle(), NOT_NULL)) {
            errors.add("title cannot be whitespace only");
        }
        if (!validateStringNotOnlySpaces(event.getInformation(), CAN_NULL)) {
            errors.add("information cannot be whitespace only");
        }
        if (!validateStringNotOnlySpaces(event.getType(), NOT_NULL)) {
            errors.add("type cannot be whitespace only");
        }
        //times
        if (!endingAfterOrsameTimeAsStart(event)) {
            errors.add("start time cannot be before end time");
        }
        if (!eventMaxOneYearInLength(event)) {
            errors.add("event max duration is one year");
        }
        if (!eventStartsAndEndWithinAYearOfNow(event)) {
            errors.add("event has to start and end within a year of now");
        }

        if (!ifHasGroupItIsKnown(event)) {
            errors.add("this group id is unknown!");
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
            return !t.getStartTime().isBefore(timeNow);

        } else {

            return false;
        }
    }

    private boolean ifHasGroupItIsKnown(Event event) {
        if (event.getGroupId() == null) {
            return true;
        }
        return groupRepository.findOne(event.getGroupId().getId()) != null;
    }
}
