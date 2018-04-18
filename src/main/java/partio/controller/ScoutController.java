package partio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import partio.service.ScoutService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
public class ScoutController {

    @Autowired
    private ScoutService scoutService;

    @PostMapping("/newscout") //this is supposed to do only when user logs in first time
    public ResponseEntity<Object> addNewScout(@RequestHeader String idTokenString) {
        GoogleIdToken idToken = null;
        try {
            idToken = scoutService.verifyId(idTokenString);
        } catch (GeneralSecurityException | IOException ex) {
            Logger.getLogger(ScoutController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ResponseEntity<Object> newScout = scoutService.findOrCreateScout(idToken);
        return newScout;
    }


    @DeleteMapping("/scouts/{scoutId}")
    public ResponseEntity<Object> deleteScout(@PathVariable Long scoutId, @RequestHeader String idTokenString) {
        GoogleIdToken idToken = null;
        try {
            idToken = scoutService.verifyId(idTokenString);
        } catch (GeneralSecurityException | IOException ex) {
            Logger.getLogger(ScoutController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return scoutService.deleteById(scoutId, idToken);
    }

}
