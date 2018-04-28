package partio.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import partio.domain.Activity;
import partio.domain.EventGroup;
import partio.repository.ActivityRepository;
import partio.repository.EventGroupRepository;

@Service
public class EventGroupService {

    @Autowired
    private EventGroupRepository groupRepository;
    @Autowired
    private ActivityBufferService bufferService;
    @Autowired
    private ActivityRepository activityRepository;

    public List<EventGroup> list() {
        return groupRepository.findAll();
    }

    //no events are accepted, this one creates a pure group
    public ResponseEntity<Object> createEventGroup() {
        EventGroup group = new EventGroup();
        groupRepository.save(group);
        return ResponseEntity.ok(group);
    }

    public ResponseEntity<Object> delete(Long groupId) {
        EventGroup toDelete = groupRepository.findOne(groupId);
        if (toDelete == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<Activity> list = activityRepository.findByEventGroupId(toDelete.getId());

        for (Activity activity : list) {
            activity.setBuffer(activity.getEvent().getScout().getBuffer());
            activity.setEvent(null);
        }
        activityRepository.save(list);
        groupRepository.delete(toDelete);
        return ResponseEntity.ok(toDelete);

    }
}
