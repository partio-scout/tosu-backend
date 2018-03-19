
package partio.domain;

import partio.jsonconfig.EventGroupSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.AbstractPersistable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@ToString(exclude = {"events"})
@JsonSerialize(using = EventGroupSerializer.class)
public class EventGroup extends AbstractPersistable<Long> {
   // @JsonManagedReference
    @OneToMany(mappedBy = "groupId", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Event> events;
    
    public EventGroup(Long is) {
        super.setId(is);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(super.getId());
        return hash;
    }
    
    public boolean equals(Object other) {
        if (other.getClass() != EventGroup.class) {
            return false;
        }
        EventGroup otherGroup = (EventGroup) other;
        return this.getId() == otherGroup.getId();
    }    

}
