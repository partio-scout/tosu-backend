package partio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import partio.domain.Scout;

public interface ScoutRepository extends JpaRepository<Scout, Long>{
    Scout findByGoogleId(String googleId);
}
