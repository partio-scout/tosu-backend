package partio.service.validators;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import partio.domain.Activity;
import partio.domain.ActivityBuffer;
import partio.domain.Scout;
import partio.repository.ActivityBufferRepository;
import partio.repository.ActivityRepository;
import partio.repository.EventRepository;

@Service
public class ActivityValidator extends Validator<Activity> {

    private static final int MAX_GUID_LENGTH = 127;
    private static final int MIN_GUID_LENGTH = 1;

    @Autowired
    private ActivityRepository acitvityRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ActivityBufferRepository bufferRepository;

    public List<String> validateUnique(Activity activity, Long scoutId) {
        List<String> errors = new ArrayList<>();
        if (!acitvityRepository.findByScoutEventsAndGuid(activity.getGuid(), scoutId).isEmpty()) {
            errors.add("player already has this activity");
        }
        if (!acitvityRepository.findByScoutBufferAndGuid(activity.getGuid(), scoutId).isEmpty()) {
            errors.add("player already has this activity");
        }
        return errors;
    }

    @Override
    public List<String> validateNew(Activity activity) {
        List<String> errors = new ArrayList<>();
        if (activity == null) {
            errors.add("passed null activity");
            return errors;
        }
        errors = validateNewAndOld(activity);

        if (!validateStringLength(activity.getGuid(), MIN_GUID_LENGTH, MAX_GUID_LENGTH, NOT_NULL)) {
            errors.add("guid length has to be between " + MIN_GUID_LENGTH + "-" + MAX_GUID_LENGTH);
        }
        //not only space in strings 
        if (!validateStringNotOnlySpaces(activity.getGuid(), NOT_NULL)) {
            errors.add("guid cannot be whitespace only");
        }

        return errors;
    }

    @Override
    public List<String> validateChanges(Activity original, Activity changes) {
        List<String> errors = new ArrayList<>();
        if (original == null || changes == null) {
            errors.add("passed null activity");
            return errors;
        }
        errors = validateNewAndOld(changes);
        if (original.getGuid().equals(changes.getGuid()) == false) {
            errors.add("guid is not allowed to change");
        }
        return errors;
    }

    @Override
    protected List<String> validateNewAndOld(Activity t) {
        List<String> errors = new ArrayList<>();

        if (t.getEvent() == null && t.getBuffer() == null) {
            errors.add("activity must belong to buffer or event");
        }

        if (t.getEvent() != null) {
            if (eventRepository.exists(t.getEvent().getId()) == false) {
                errors.add("event of activity is not found in db.");
            }
        }
        if (t.getBuffer() != null) {
            ActivityBuffer bufferInDb = bufferRepository.findOne(t.getBuffer().getId());
            if (bufferInDb == null) {
                errors.add("buffer of activity is not found in db.");
            } else if (bufferInDb.getActivities() != null
                    && bufferInDb.getActivities().size() >= ActivityBuffer.BUFFER_SIZE) {
                errors.add("buffer is full.");
            }
            if (t.getEvent() != null) {
                errors.add("Activity can exist only in event OR in buffer");
            }
        }

        return errors;
    }
}
