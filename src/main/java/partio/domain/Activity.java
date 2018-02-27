
package partio.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
import partio.jsonconfig.ActivitySerializer;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonSerialize(using = ActivitySerializer.class)
public class Activity  extends AbstractPersistable<Long> {  
      
    //turha lisäys
    @ManyToOne
    @JoinColumn    
    private Event event;   
    //pof backend id
    @Column(unique=true)
    private String guid;
}
