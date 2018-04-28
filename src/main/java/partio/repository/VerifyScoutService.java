package partio.repository;

import java.util.List;
import java.util.Objects;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import partio.domain.Activity;
import partio.domain.Event;
import partio.domain.Plan;
import partio.domain.Scout;

@Service
@Transactional
public class VerifyScoutService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ActivityBufferRepository bufferRepository;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private ScoutRepository scoutRepository;
    @Autowired
    private EventGroupRepository groupRepository;

    // user is logged in
    public boolean isLoggedIn(Scout scout) {
        try {
            return scoutRepository.findOne(scout.getId()) != null;
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

    public boolean isOwnerForBuffer(Long bufferId, Scout scout) {
        try {
            return Objects.equals(scout.getId(), bufferRepository.getOne(bufferId).getScout().getId());
        } catch (NullPointerException | EntityNotFoundException e) {
            return false;
        }
    }

    // is activity.event.scout == scout
    //keep in mind activity can be either in event OR in buffer
    public boolean isOwnerForActivity(Long activityId, Scout scout) {

        try {
            scout = scoutRepository.findOne(scout.getId());
            Activity activity = activityRepository.getOne(activityId);
            if (activity.getBuffer() != null) {
                return Objects.equals(scout.getId(), activityRepository.getOne(activityId).getBuffer().getScout().getId());
            } else {
                return Objects.equals(scout.getId(), activityRepository.getOne(activityId).getEvent().getScout().getId());
            }
        } catch (NullPointerException | EntityNotFoundException e) {
            System.out.println("exce");
            return false;
        }
    }

    // is plan.activity.event.scout == scout
    //keep in mind plan's parent activity can be either in event OR in buffer
    public boolean isOwnerForPlan(Long planId, Scout scout) {
        try {
            scout = scoutRepository.findOne(scout.getId());
            Plan plan = planRepository.getOne(planId);
            if (plan.getActivity().getBuffer() != null) {
                return Objects.equals(scout.getId(), planRepository.getOne(planId).getActivity().getBuffer().getScout().getId());
            } else {
                return Objects.equals(scout.getId(), planRepository.getOne(planId).getActivity().getEvent().getScout().getId());
            }
        } catch (NullPointerException | EntityNotFoundException e) {
            return false;
        }
    }

    //is eventGroup.scout == scout
    public boolean isOwnerForEventGroup(Long groupId, Scout scout) {
        try {
            List<Event> events = groupRepository.getOne(groupId).getEvents();
            return Objects.equals(scout.getId(), groupRepository.getOne(groupId).getEvents().get(0).getScout().getId());
        } catch (NullPointerException | EntityNotFoundException e) {
            return false;
        }
    }
}
