package partio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import partio.domain.Scout;
import partio.service.ScoutService;

@RestController
public class ScoutController {
    
    @Autowired
    private ScoutService scoutService;
    
    @PostMapping("/newscout") //this is supposed to do only when user logs in first time
    public ResponseEntity<Object> addNewScout(@RequestBody Scout scout) {     
        ResponseEntity<Object> newScout = scoutService.addNewScout(scout);
        return newScout;
    }
    
        @DeleteMapping("/scouts/{scoutId}")
    public ResponseEntity<Object> deleteScout(@PathVariable Long scoutId) {
        return scoutService.deleteById(scoutId);
    }
    
}
