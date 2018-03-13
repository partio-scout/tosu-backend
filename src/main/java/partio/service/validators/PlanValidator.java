package partio.service.validators;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import partio.domain.Plan;
import partio.repository.ActivityRepository;
import partio.repository.PlanRepository;

@Service
public class PlanValidator extends Validator<Plan> {

    private static final int MAX_GUID_LENGTH = 127;
    private static final int MIN_GUID_LENGTH = 1;

    @Autowired
    private ActivityRepository acitvityRepository;

    @Autowired
    private PlanRepository planRepository;

    @Override
    public List<String> validateNew(Plan plan) {
        List<String> errors = validateNewAndOld(plan);
        if (!validateStringLength(plan.getGuid(), MIN_GUID_LENGTH, MAX_GUID_LENGTH, NOT_NULL)) {
            errors.add("guid length has to be between " + MIN_GUID_LENGTH + "-" + MAX_GUID_LENGTH);
        }

        if (!validateStringNotOnlySpaces(plan.getGuid(), NOT_NULL)) {
            errors.add("guid cannot be whitespace only");
        }
        
        return errors;
    }

    @Override
    public List<String> validateChanges(Plan original, Plan changes) {
        validateNew(changes);
        validateNewAndOld(original);
        List<String> errors = new ArrayList<>();
        return errors;
    }

    @Override
    protected List<String> validateNewAndOld(Plan plan) {
        List<String> errors = new ArrayList<>();

        if (plan.getActivity() != null) {
            errors.add("Execution plan can't exist without activity");
        }

        return errors;
    }



}
