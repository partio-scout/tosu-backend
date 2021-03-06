package partio.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
import partio.jsonconfig.ActivityBufferSerializer;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@JsonSerialize(using = ActivityBufferSerializer.class)
public class ActivityBuffer extends AbstractPersistable<Long> {

    public static final int BUFFER_SIZE = 10;
    
    @OneToMany(mappedBy = "buffer", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Activity> activities;
    
    @OneToOne
    private Scout scout;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(super.getId());
        return hash;
    }

    @Override
    public boolean equals(Object other) {
        if (other.getClass() != ActivityBuffer.class) {
            return false;
        }
        ActivityBuffer otherGroup = (ActivityBuffer) other;
        return Objects.equals(this.getId(), otherGroup.getId());
    }

}
