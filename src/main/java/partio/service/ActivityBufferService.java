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
import partio.repository.ActivityBufferRepository;
import partio.repository.ActivityRepository;
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

    //because we dont have multiuser support yet i made it to create one if one does not exist
    //will always return same one
    public ActivityBuffer findBuffer(Long id) {
        if (bufferRepository.count() == 0) {
            ActivityBuffer buffer = new ActivityBuffer();
            bufferRepository.save(buffer);
            return buffer;

        } else {
            return bufferRepository.findAll().get(0);
        }
    }

    public ResponseEntity<Object> getBufferContent(Long id) {
        ActivityBuffer buffer = findBuffer(id);
        System.out.println(buffer);
        if (buffer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(buffer);
    }

    public ResponseEntity<Object> addActivity(Long ActivityBufferId, Activity activity) {
        ActivityBuffer buffer = findBuffer(ActivityBufferId);
        if (buffer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        activity.setBuffer(buffer);
        List<String> errors = activityValidator.validateNew(activity);
        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        activityRepository.save(activity);
        if (buffer.getActivities() == null) {
            buffer.setActivities(new ArrayList<>());
        }
        buffer.getActivities().add(activity);

        bufferRepository.save(buffer);
        return ResponseEntity.ok(buffer);
    }

}
