
package partio.repository;

import java.util.List;
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
    public boolean isLoggedIn(Scout scout){
        if(scout == null){
            return false;
        }
        if(scoutRepository.findOne(scout.getId())==null){
            return false;
        }
        return true;
    }
    
   
    // is event.scout == scout
    public boolean isOwnerForEvent(Long eventId, Scout scout){
        return scoutRepository.findOne(scout.getId())==eventRepository.getOne(eventId).getScout();
    }
    
    
    // is activity.event.scout == scout
    public boolean isOwnerForActivity(Long activityId, Scout scout){
        return scoutRepository.findOne(scout.getId())==activityRepository.getOne(activityId).getEvent().getScout();
    }
    
    //is eventGroup.scout == scout
    public boolean isOwnerForEventGroup(Long groupId, Scout scout) { 
        List<Event> events = groupRepository.getOne(groupId).getEvents();
        if(events.isEmpty() || events ==null ){
            return false;
        }
        return scoutRepository.findOne(scout.getId())==groupRepository.getOne(groupId).getEvents().get(0).getScout();   
    }
}
