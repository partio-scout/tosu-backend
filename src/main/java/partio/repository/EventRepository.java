package partio.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import partio.domain.Event;

public interface EventRepository extends JpaRepository<Event, Long>{
     List<Event> findByEndDateAfter(LocalDate date);

}
