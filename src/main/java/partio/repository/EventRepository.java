package partio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import partio.domain.Event;
import partio.domain.EventGroup;

public interface EventRepository extends JpaRepository<Event, Long>{
    long countByGroupId(EventGroup group);
     

}
