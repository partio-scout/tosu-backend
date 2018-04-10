package partio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import partio.domain.User;

public interface UserRepository extends JpaRepository<User, Long>{
    
}
