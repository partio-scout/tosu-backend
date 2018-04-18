package partio.service;


import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import java.io.IOException;
import java.security.GeneralSecurityException;
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

    public Scout findScoutByGoogleId(GoogleIdToken idToken) {
        if (idToken != null) {
            Payload payload = idToken.getPayload();

            String userId = payload.getUserId();
            Scout scout = scoutRepository.findByGoogleId(userId);
            if (scout != null) {
                return scout;
            }
        }
        return null;
    }

    public GoogleIdToken verifyId(String idTokenString) throws GeneralSecurityException, IOException {

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
