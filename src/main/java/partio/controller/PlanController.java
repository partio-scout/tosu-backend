package partio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import partio.domain.Plan;
import partio.service.PlanService;


@RestController
@Scope(value = "session")
public class PlanController {

    @Autowired
    private PlanService planService;

    //FOR INSPECTING NO REAL USE
   // @GetMapping("/activity/{activityId}/plans")
    //public ResponseEntity<Object> getPlansForActivity(@PathVariable Long activityId) {
     //   return planService.list(activityId);
    //}

    @PostMapping("/activity/{activityId}/plans")
    public ResponseEntity<Object> addPlanForActivity(@PathVariable Long activityId, @RequestBody Plan jsonPlan) {
        return planService.addPlan(activityId, jsonPlan);
    }
    
    @PutMapping("/plans/{planId}")
    public ResponseEntity<Object> modifyPlan(@PathVariable Long planId, @RequestBody Plan jsonPlan) {
        return planService.modifyPlan(jsonPlan, planId);
    }

    @DeleteMapping("/plans/{planId}")
    public ResponseEntity<Object> deletePlan(@PathVariable Long planId) {
        return planService.removePlan(planId);
    }
}
