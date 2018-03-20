package partio.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
import partio.jsonconfig.PlanSerializer;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@JsonSerialize(using = PlanSerializer.class)
public class Plan extends AbstractPersistable<Long> {
    
    @ManyToOne
    @JoinColumn
    private Activity activity;
    
    private String title;
    private String content;
}
