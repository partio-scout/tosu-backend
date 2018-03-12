package partio.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import partio.domain.Activity;
import partio.domain.Plan;
import partio.service.PlanService;

@RestController
public class PlanController {

    @Autowired
    private PlanService planService;

    @GetMapping("/activity/{activityId}/plan")
    public List<Plan> getPlansForActivity(@PathVariable Long activityId) {
        return planService.list(activityId);
    }

    @PostMapping("/activity/{activityId}/plans")
    public ResponseEntity<Object> addPlanForActivity(@PathVariable Long activityId, @RequestBody Plan jsonPlan) {
        return planService.addPlan(activityId, jsonPlan);
    }

    @DeleteMapping("/plans/{planId}")
    public ResponseEntity<Object> deletePlan(@PathVariable Long planId) {
        return planService.removePlan(planId);
    }
}
