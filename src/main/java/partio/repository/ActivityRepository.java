package partio.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import partio.domain.Activity;
import partio.domain.Event;

public interface ActivityRepository extends JpaRepository<Activity, Long>{
 //    List<Activity> findByEvent(Event event);

}

