package partio.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import partio.domain.Activity;
import partio.domain.Event;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> findByEvent(Event event);

    Activity findByGuid(String guid);
    @Query("SELECT a FROM Activity a LEFT JOIN a.event e LEFT JOIN e.groupId g WHERE g.id = ?1") 
    List<Activity> findByEventGroupId(Long groupId);

}
