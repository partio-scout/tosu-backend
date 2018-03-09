package partio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import partio.domain.Plan;


public interface PlanRepository extends JpaRepository<Plan, Long>{
    
}
