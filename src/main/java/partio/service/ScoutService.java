package partio.service;

import org.springframework.beans.factory.annotation.Autowired;
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

    public ResponseEntity<Object> addNewScout(Scout scout) {
        Scout existingScout = scoutRepository.findByGoogleId(scout.getGoogleId());
        if (existingScout != null) { //If scout allready exist, don't add same scout twice.
            return ResponseEntity.ok(existingScout);
        }
        scoutRepository.save(scout);
        return ResponseEntity.ok(scout);
    }
}
