
package partio.domain;

import partio.jsonconfig.EventGroupSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@JsonSerialize(using = EventGroupSerializer.class)
public class EventGroup extends AbstractPersistable<Long> {
   // @JsonManagedReference
    @OneToMany(mappedBy = "groupId", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Event> events;
    
    public EventGroup(Long is) {
        super.setId(is);
    }
    

}
