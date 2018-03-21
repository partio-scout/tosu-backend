package partio.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.AbstractPersistable;
import partio.jsonconfig.ActivitySerializer;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"buffer", "activity"})
@JsonSerialize(using = ActivitySerializer.class)
public class Activity extends AbstractPersistable<Long> {

    @ManyToOne
    @JoinColumn
    private Event event;

    @ManyToOne
    @JoinColumn
    private ActivityBuffer buffer;
    //pof backend id
    @OneToMany(mappedBy = "activity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Plan> plans;

    private String guid;

    public Activity(String guid) {
        this.guid = guid;
    }

    //tests need this constructor
    public Activity(Event event, String guid) {
        this.guid = guid;
        this.event = event;
    }

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
