package partio.service;

import java.util.ArrayList;
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
import partio.service.validators.ActivityValidator;

@Service
@Transactional
public class ActivityBufferService {

    @Autowired
    private ActivityBufferRepository bufferRepository;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private ActivityValidator activityValidator;
    @Autowired 
    ScoutRepository scoutRepository;

    public ResponseEntity<Object> getBufferOfScout(Scout scout) {
        ActivityBuffer buffer = bufferRepository.findByScout(scout);
        if (buffer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(buffer);
    }

    public ResponseEntity<Object> addActivityToBuffer(Activity activity, Scout scout) {
        ActivityBuffer buffer = bufferRepository.findByScout(scout);
        if (buffer == null) {
            buffer = new ActivityBuffer(new ArrayList(), scout);
            bufferRepository.save(buffer);
        }

        activity.setBuffer(buffer);

        List<String> errors = activityValidator.validateNew(activity);
        errors.addAll(activityValidator.validateUnique(activity, scout.getId()));
        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        //saves to bufferzone as well because join column is in activity
        activityRepository.save(activity);

        return ResponseEntity.ok(activity);
    }

}
