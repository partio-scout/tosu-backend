
package partio.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import org.springframework.data.jpa.domain.AbstractPersistable;


public class EventGroup extends AbstractPersistable<Long> {
    @JsonManagedReference
    @OneToMany(mappedBy = "group", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Event> events;
}
