package partio.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import partio.domain.Activity;
import partio.domain.Plan;
import partio.repository.ActivityRepository;
import partio.repository.PlanRepository;

@Service
@Transactional
public class PlanService {

    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private PlanRepository planRepository;

    public ResponseEntity<Object> removePlan(Long planId) {
        Plan toDelete = planRepository.findOne(planId);
        if (toDelete == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        
        planRepository.delete(toDelete);
        return ResponseEntity.ok(toDelete);
    }

    public ResponseEntity<Object> addPlan(Long activityId, Plan plan) {
        Activity activity = activityRepository.getOne(activityId);
        activity.getPlans().add(plan);

        plan.setActivity(activity);

        planRepository.save(plan);
        activityRepository.save(activity);

        return ResponseEntity.ok(plan);
    }

    public List<Plan> list(Long activityId) {
        return activityRepository.getOne(activityId).getPlans();
    }

}
