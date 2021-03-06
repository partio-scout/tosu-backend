package partio.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import partio.domain.Activity;
import partio.domain.ActivityBuffer;
import partio.domain.Scout;
import partio.repository.ActivityBufferRepository;
import partio.repository.ActivityRepository;
import partio.repository.ScoutRepository;

@Service
@Transactional
public class ScoutService {

    @Autowired
    private ScoutRepository scoutRepository;
    @Autowired
    private ActivityBufferRepository bufferRepository;
    @Autowired
    private ActivityRepository activityRepository;

    /*
    If user isn't in scoutRepository, he will be added. Return created or found scout.
    */
    public ResponseEntity<Object> findOrCreateScout(GoogleIdToken idToken) throws NullPointerException {
        System.out.println("enter");
        Payload payload = idToken.getPayload();
        String userId = payload.getSubject();
        Scout existingScout = scoutRepository.findByGoogleId(userId);
        
        
        if (existingScout != null) { //If scout already exists, don't add same scout twice.
            System.out.println("found old");
            return ResponseEntity.ok(existingScout);
        }

        Scout scout = new Scout();
        scout.setGoogleId(userId);
        scout.setName((String) payload.get("name"));
        System.out.println("return new");
        scoutRepository.save(scout);
        ActivityBuffer buffer = new ActivityBuffer(new ArrayList<>(), scout);
        bufferRepository.save(buffer);
        
        return ResponseEntity.ok(scout);
    }
    /*
    Here user can remove his account.
    */
    public ResponseEntity<Object> deleteById(Scout scout) {
            if (!scoutRepository.exists(scout.getId())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            List<Activity> activities  = activityRepository.findByScoutEvents(scout.getId());
            scoutRepository.delete(scout);
            activityRepository.delete(activities);
            return ResponseEntity.ok(scout);
    }
    
    /*
    Help-method to find uer by googleId.
    */
    public Scout findScoutByGoogleId(GoogleIdToken idToken) {
        if (idToken != null) {
            Payload payload = idToken.getPayload();

            String userId = payload.getSubject();
            Scout scout = scoutRepository.findByGoogleId(userId);
            if (scout != null) {
                return scout;
            }
        }
        return null;
    }

    /*
    Google idToken verifier, https://developers.google.com/identity/sign-in/web/backend-auth
    */
    public GoogleIdToken verifyId(String idTokenString) throws GeneralSecurityException, IOException, IllegalArgumentException {

        JacksonFactory jacksonFactory = new JacksonFactory();
        HttpTransport transport = new ApacheHttpTransport();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList("7360124073-8f1bq4mul415hr3kdm154vq3c65lp36d.apps.googleusercontent.com"))
                // Or, if multiple clients access the backend:
                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build();

        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken != null) {
            return idToken;
        }

        return null;

    }

}
