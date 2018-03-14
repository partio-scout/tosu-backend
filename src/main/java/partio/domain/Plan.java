package partio.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Plan extends AbstractPersistable<Long> {
    
    @ManyToOne
    @JoinColumn
    private Activity activity;
    
    private String guid;
    //not sure yet what else will come here
}
