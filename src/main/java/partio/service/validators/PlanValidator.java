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
        List<String> errors = new ArrayList<>();
        return errors;
    }

    @Override
    protected List<String> validateNewAndOld(Plan plan) {
        List<String> errors = new ArrayList<>();

        String url = plan.getUrl();
        if (plan.getActivity() != null) {
            errors.add("Execution plan can't exist without activity");
        }
        if (!isValid(plan.getUrl())) {
            errors.add("Url isn't valid.");
        }

        return errors;
    }

    private boolean isValid(String url) {
        //coming soon
        return true;
    }

}
