package partio.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import partio.domain.Event;
import partio.domain.EventGroup;
import partio.domain.Scout;


public interface EventRepository extends JpaRepository<Event, Long>{
    long countByGroupId(EventGroup group);
    List<Event> findByScout(Scout scout);
        
}

