package partio.service.validators;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import partio.domain.Activity;
import partio.domain.Event;
import partio.repository.ActivityRepository;

@Service
public class ActivityValidator extends Validator<Activity> {

    private static final int MAX_GUID_LENGTH = 127;
    private static final int MIN_GUID_LENGTH = 1;
    
    @Autowired
    private ActivityRepository acitvityRepository;

    @Override
    public List<String> validateNew(Activity activity) {
        List<String> errors = new ArrayList<>();

        if (!validateStringLength(activity.getGuid(), MIN_GUID_LENGTH, MAX_GUID_LENGTH, NOT_NULL)) {
            errors.add("guid length has to be between " + MIN_GUID_LENGTH + "-" + MAX_GUID_LENGTH);
        }
        //not only space in strings 
        if (!validateStringNotOnlySpaces(activity.getGuid(), NOT_NULL)) {
            errors.add("guid cannot be whitespace only");
        }
        
        if (activityWithSameGuidExists((activity.getGuid()))) {
           errors.add("activity with same guid already exists");
        }      
        
        return errors;
    }

    @Override
    public List<String> validateChanges(Event original, Event changes) {
        List<String> errors = new ArrayList<>();
        errors.add("you are not allowed to modify this");
        return errors;
    }

    @Override
    protected List<String> validateNewAndOld(Activity t) {
        throw new UnsupportedOperationException("Not supported yet. Activity cannot be modified anyways."); //To change body of generated methods, choose Tools | Templates.

    }

    private boolean activityWithSameGuidExists(String guid) {
         if (acitvityRepository.findByGuid(guid) == null) {
             return false;
         }
         return true;
    }

}
