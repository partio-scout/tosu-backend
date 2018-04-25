package partio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import partio.service.ScoutService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import java.io.IOException;
import java.security.GeneralSecurityException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@Scope(value = "session")
public class ScoutController {

    @Autowired
    private ScoutService scoutService;

    @PostMapping("/scout") //this is supposed to do only when user logs in first time
    public ResponseEntity<Object> registerOrLoginScout(@RequestHeader String Authorization, HttpServletRequest request, HttpSession session) {
       try {
            GoogleIdToken idToken = scoutService.verifyId(Authorization);
            ResponseEntity<Object> newScout = scoutService.findOrCreateScout(idToken);

            session.invalidate();
            HttpSession newSession = request.getSession();
            
            newSession.setAttribute("scout", idToken.getPayload().getSubject());
            return newScout;
        } catch (GeneralSecurityException | IOException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex);
        }

    }

    @DeleteMapping("/scouts/{scoutId}")
    public ResponseEntity<Object> deleteScout(@RequestHeader String idTokenString) {
        
        try {
            GoogleIdToken idToken = scoutService.verifyId(idTokenString);
            return scoutService.deleteById(idToken);
        } catch (GeneralSecurityException | IOException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex);
        }

    }

}
