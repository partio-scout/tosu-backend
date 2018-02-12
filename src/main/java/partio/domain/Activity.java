
package partio.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Activity  extends AbstractPersistable<Long> {  
    
    @JsonBackReference   
    @ManyToOne
    @JoinColumn    
    private Event event;    
    private String information;
    
    //not sure if necessary was useful in testing
    private long eventid;

}
