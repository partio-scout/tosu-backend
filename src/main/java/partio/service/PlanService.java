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
        //getOne voi heittää exceptionin jos aktiviteettiä ei löydy
        Activity activity = activityRepository.getOne(activityId);

        if (activity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<String> errors = validator.validateNew(plan);
        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        //tää kait kuuluu validointiin, kaikki tarkastukset sinne paitsi 404 errorit
        for(Plan planExist : activity.getPlans()){
            if(planExist.getGuid().equals(plan.getGuid())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Plan may only be added to activity once.");
            }
        }
        
        //nullpointteri jos aktiviteetin lista on tyhjä
        activity.getPlans().add(plan);
        plan.setActivity(activity);

        planRepository.save(plan);
        //planissa on se viite niin aktiviteettiin ei tuu muutoksia eli ei tarvi tallentaa
      //  activityRepository.save(activity);

        return ResponseEntity.ok(plan);
    }

    public List<Plan> list(Long activityId) {
        //exception getone kanssa mahdollista + nullpointer jos ei ole planeja
        return activityRepository.getOne(activityId).getPlans();
    }

}
