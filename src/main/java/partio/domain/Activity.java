package partio.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
//import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
import partio.jsonconfig.ActivitySerializer;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
//@EqualsAndHashCode(callSuper = false)
@JsonSerialize(using = ActivitySerializer.class)
public class Activity extends AbstractPersistable<Long> {

    @ManyToOne
    @JoinColumn
    private Event event;
    //pof backend id
    private String guid;

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object.getClass() == Activity.class) {
            Activity obj = (Activity) object;
            return this.guid.equals(obj.guid);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.guid);
        return hash;
    }
}
