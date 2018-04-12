package partio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import partio.service.ScoutService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

@RestController
public class ScoutController {

    @Autowired
    private ScoutService scoutService;

    @PostMapping("/newscout") //this is supposed to do only when user logs in first time
    public ResponseEntity<Object> addNewScout(@RequestBody GoogleIdToken idToken) {
        ResponseEntity<Object> newScout = scoutService.findOrCreateScout(idToken);
        return newScout;
    }

    @DeleteMapping("/scouts/{scoutId}")
    public ResponseEntity<Object> deleteScout(@PathVariable Long scoutId, @RequestBody GoogleIdToken idToken) {
        return scoutService.deleteById(scoutId, idToken);
    }

}
