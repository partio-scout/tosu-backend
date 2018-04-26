package partio.repository;

import java.util.List;
import java.util.Objects;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import partio.domain.Event;
import partio.domain.Scout;

@Service
@Transactional
public class VerifyScoutService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private ScoutRepository scoutRepository;
    @Autowired
    private EventGroupRepository groupRepository;

    // user is logged in
    public boolean isLoggedIn(Scout scout) {
        try {
            return scoutRepository.findOne(scout.getId()) == null;
        } catch (NullPointerException | EntityNotFoundException e) {
            return false;
        }
    }

    // is event.scout == scout
    public boolean isOwnerForEvent(Long eventId, Scout scout) {
        try {
            return scoutRepository.findOne(scout.getId()) == eventRepository.getOne(eventId).getScout();
        } catch (NullPointerException | EntityNotFoundException e) {
            return false;
        }
    }

    // is activity.event.scout == scout
    public boolean isOwnerForActivity(Long activityId, Scout scout) {
        System.out.println(scout);
        try {
            return scoutRepository.findOne(scout.getId()) == activityRepository.getOne(activityId).getEvent().getScout();
        } catch (NullPointerException | EntityNotFoundException e) {
            return false;
        }
    }

    //is eventGroup.scout == scout
    public boolean isOwnerForEventGroup(Long groupId, Scout scout) {
        try {
            List<Event> events = groupRepository.getOne(groupId).getEvents();
            if (events.isEmpty() || events == null) {
                System.out.println("group empty");
                return false;
            }
            System.out.println(scout + " in verifier");
            System.out.println(Objects.equals(scout.getId(), groupRepository.getOne(groupId).getEvents().get(0).getScout().getId()));
            return Objects.equals(scout.getId(), groupRepository.getOne(groupId).getEvents().get(0).getScout().getId());
        } catch (NullPointerException | EntityNotFoundException e) {
            System.out.println("caught an error");
            return false;
        }
    }
}
