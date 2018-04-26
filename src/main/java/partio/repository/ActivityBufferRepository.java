
package partio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import partio.domain.ActivityBuffer;
import partio.domain.Scout;

public interface ActivityBufferRepository extends JpaRepository<ActivityBuffer, Long>{

    public ActivityBuffer findByScout(Scout scoutId);

}