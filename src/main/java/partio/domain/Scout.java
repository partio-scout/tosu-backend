package partio.domain;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Scout extends AbstractPersistable<Long> {

    String googleId;
    @OneToMany(mappedBy = "scout", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)   
    private List<Event> events;   
//    @OneToOne(mappedBy = "scout")
    private ActivityBuffer buffer;
    String name;
    
    
}
