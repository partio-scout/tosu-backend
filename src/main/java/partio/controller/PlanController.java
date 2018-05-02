package partio.controller;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import partio.domain.Plan;
import partio.domain.Scout;
import partio.service.PlanService;
import partio.repository.VerifyScoutService;

@RestController
@Scope(value = "session")
public class PlanController {

    @Autowired
    private PlanService planService;
    @Autowired
    private VerifyScoutService verifyScoutService;

    //FOR INSPECTING NO REAL USE
    // @GetMapping("/activity/{activityId}/plans")
    //public ResponseEntity<Object> getPlansForActivity(@PathVariable Long activityId) {
    //   return planService.list(activityId);
    //}
    @PostMapping("/activity/{activityId}/plans")
    public ResponseEntity<Object> addPlanForActivity(@PathVariable Long activityId, @RequestBody Plan jsonPlan,
            HttpSession session) {
        Scout scout = (Scout) session.getAttribute("scout");
        System.out.println(activityId);
        if (!verifyScoutService.isOwnerForActivity(activityId, scout)) {
            System.out.println("feil login");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("you are not owner of this event group!");
        }

        System.out.println("enter service");
        return planService.addPlan(activityId, jsonPlan);
    }

    @PutMapping("/plans/{planId}")
    public ResponseEntity<Object> modifyPlan(@PathVariable Long planId, @RequestBody Plan jsonPlan, HttpSession session) {

        Scout scout = (Scout) session.getAttribute("scout");
        if (!verifyScoutService.isOwnerForPlan(planId, scout)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("you are not owner of this event group!");
        }
        return planService.modifyPlan(jsonPlan, planId);
    }

    @DeleteMapping("/plans/{planId}")
    public ResponseEntity<Object> deletePlan(@PathVariable Long planId, HttpSession session) {
        Scout scout = (Scout) session.getAttribute("scout");
        if (!verifyScoutService.isOwnerForPlan(planId, scout)) {
            System.out.println("not owner of plan");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("you are not owner of this event group!");
        }
        return planService.removePlan(planId);
    }
}
