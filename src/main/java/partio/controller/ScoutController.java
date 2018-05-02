package partio.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
    public ResponseEntity<Object> registerOrLoginScout(@RequestBody ObjectNode Authorization, HttpSession session) {
       
        try {
            System.out.println(Authorization.get("Authorization").asText());
            GoogleIdToken idToken = scoutService.verifyId(Authorization.get("Authorization").asText());
            ResponseEntity<Object> newScout = scoutService.findOrCreateScout(idToken);

            session.setAttribute("scout", scoutRepo.findByGoogleId(idToken.getPayload().getSubject()));
            return newScout;
            //many cases of failing login due to invalid token or expired so wrapped in try-catch
        } catch (GeneralSecurityException | IOException | IllegalArgumentException | NullPointerException ex) {
            session.invalidate();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex);
        }
    }
    

    @DeleteMapping("/scouts")
    public ResponseEntity<Object> deleteScout(HttpSession session) {
        try {
            ResponseEntity result = scoutService.deleteById((Scout) session.getAttribute("scout"));
            session.invalidate();
            return result;
            //either no session or doesnt exist
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }

}
