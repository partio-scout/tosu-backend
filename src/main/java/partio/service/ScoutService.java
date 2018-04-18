package partio.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import java.util.Arrays;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import partio.domain.Scout;
import partio.repository.ScoutRepository;

@Service
@Transactional
public class ScoutService {

    @Autowired
    private ScoutRepository scoutRepository;

    public ResponseEntity<Object> findOrCreateScout(GoogleIdToken idToken) {
        Payload payload = idToken.getPayload();

        String userId = payload.getUserId();
        Scout existingScout = scoutRepository.findByGoogleId(userId);
       
        if (existingScout == null) { //If scout allready exist, don't add same scout twice.
            return ResponseEntity.ok(existingScout);
        }
         
        
        Scout scout = new Scout();
        scout.setGoogleId(userId);
        scout.setName((String) payload.get("name"));
        
        
        scoutRepository.save(scout);
        return ResponseEntity.ok(scout);
    }

    public ResponseEntity<Object> deleteById(Long scoutId, GoogleIdToken idToken) {

        if (idToken != null) {
            Payload payload = idToken.getPayload();

            String userId = payload.getUserId();
            Scout scout = scoutRepository.findByGoogleId(userId);

            Scout toDelete = scoutRepository.findOne(scoutId);

            if (toDelete == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            if (toDelete.getGoogleId() == null ? scout.getGoogleId() != null : !toDelete.getGoogleId().equals(scout.getGoogleId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            scoutRepository.delete(toDelete);
            return ResponseEntity.ok(toDelete);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    
    public Scout findScoutByGoogleId(GoogleIdToken idToken){
        if (idToken != null) {
            Payload payload = idToken.getPayload();

            String userId = payload.getUserId();
            Scout scout = scoutRepository.findByGoogleId(userId);
            if(scout != null){
                return scout;
            }
        }
        return null;
    }

}
