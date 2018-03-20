package partio.service;

import java.util.ArrayList;
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
import partio.service.validators.PlanValidator;

@Service
@Transactional
public class PlanService {

    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private PlanValidator validator;

    public ResponseEntity<Object> removePlan(Long planId) {
        Plan toDelete = planRepository.findOne(planId);
        if (toDelete == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        
        planRepository.delete(toDelete);
        return ResponseEntity.ok(toDelete);
    }

    public ResponseEntity<Object> addPlan(Long activityId, Plan plan) {
        Activity activity = activityRepository.findOne(activityId);
        if (activity == null) {    
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        
        //set defaults if empty..ask customer more about it. If defaults are fine can remove validation later.
        if (plan.getContent() == null || plan.getContent().trim().isEmpty()) {
            plan.setContent("Tyhjä suunnitelma");
        }
        if (plan.getTitle() == null || plan.getTitle().trim().isEmpty()) {
            plan.setTitle("Tyhjä otsikko");
        }
        plan.setActivity(activity);
        
        List<String> errors = validator.validateNew(plan);
        if (!errors.isEmpty()) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        planRepository.save(plan);
        return ResponseEntity.ok(plan);
    }
    
    public ResponseEntity<Object> modifyPlan(Plan plan, Long planId) {
        if (plan == null) {    
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        
        Plan original = planRepository.findOne(planId);    
        List<String> errors = validator.validateChanges(original, plan);
        if (!errors.isEmpty()) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        original.setContent(plan.getContent());
        original.setTitle(plan.getTitle());
        planRepository.save(original);
        return ResponseEntity.ok(original);
    }

    public ResponseEntity<Object> list(Long activityId) {
        Activity activity = activityRepository.findOne(activityId);
        if (activity == null) {    
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(activity.getPlans());
    }

}
