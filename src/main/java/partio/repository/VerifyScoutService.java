
package partio.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
}
