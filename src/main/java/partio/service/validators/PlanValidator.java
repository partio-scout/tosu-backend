package partio.service.validators;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import partio.domain.Plan;
import partio.repository.ActivityRepository;
import partio.repository.PlanRepository;

@Service
public class PlanValidator extends Validator<Plan> {

    @Autowired
    private ActivityRepository acitvityRepository;

    @Autowired
    private PlanRepository planRepository;

    @Override
    public List<String> validateNew(Plan plan) {
        List<String> errors = validateNewAndOld(plan);
        return errors;
    }

    @Override
    public List<String> validateChanges(Plan original, Plan changes) {
        List<String> errors = validateNewAndOld(changes);
        if (original == null || changes == null) {
            errors.add("plan or original plan is null");
            return errors;
        }
        if (original.getId() == null) {
            errors.add("id not found from original plan");
        } else if (planRepository.findOne(original.getId()) == null) {
            errors.add("plan not found in database");
        }
        return errors;
    }

    @Override
    protected List<String> validateNewAndOld(Plan plan) {
        List<String> errors = new ArrayList<>();
        if (plan == null) {
            errors.add("plan cannot be null");
            return errors;
        }
        if (!validateStringNotOnlySpaces(plan.getTitle(), Validator.NOT_NULL)) {
            errors.add("Title cannot be null or spaces only");
        }
        if (!validateStringNotOnlySpaces(plan.getContent(), Validator.NOT_NULL)) {
            errors.add("Content cannot be null or spaces only");
        }
        
        return errors;
    }
}
