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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import partio.domain.Scout;
import partio.repository.ScoutRepository;

@RestController
@Scope(value = "session")
public class ScoutController {
    @Autowired
    private ScoutRepository scoutRepo;
    @Autowired
    private ScoutService scoutService;

    @PostMapping("/scout") //this is supposed to do only when user logs in first time
    public ResponseEntity<Object> registerOrLoginScout(@RequestHeader String Authorization, HttpServletRequest request, HttpSession session) {
       try {
            GoogleIdToken idToken = scoutService.verifyId(Authorization);
            ResponseEntity<Object> newScout = scoutService.findOrCreateScout(idToken);

           // session.invalidate();
           // HttpSession newSession = request.getSession();
            
            session.setAttribute("scout", scoutRepo.findByGoogleId(idToken.getPayload().getSubject()));
            return newScout;
        } catch (GeneralSecurityException | IOException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex);
        }

    }
    
   @RequestMapping(value="/scout", method=RequestMethod.OPTIONS) //this is supposed to do only when user logs in first time
    public ResponseEntity<Object> registerOrLoginScout2(@RequestHeader String Authorization, HttpServletRequest request, HttpSession session) {
       try {
            GoogleIdToken idToken = scoutService.verifyId(Authorization);
            ResponseEntity<Object> newScout = scoutService.findOrCreateScout(idToken);

           // session.invalidate();
           // HttpSession newSession = request.getSession();
            
            session.setAttribute("scout", scoutRepo.findByGoogleId(idToken.getPayload().getSubject()));
            return newScout;
        } catch (GeneralSecurityException | IOException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex);
        }

    }

    @DeleteMapping("/scouts/{scoutId}")
    public ResponseEntity<Object> deleteScout(HttpSession session) {
            ResponseEntity result = scoutService.deleteById((Scout) session.getAttribute("scout"));
            session.invalidate();
            return result;
    }

}
