package partio.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.AbstractPersistable;
import partio.jsonconfig.PlanSerializer;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@ToString(exclude = {"activity"})
//@Proxy(lazy=false)
@JsonSerialize(using = PlanSerializer.class)
public class Plan extends AbstractPersistable<Long> {
    
    @ManyToOne
    @JoinColumn
    private Activity activity;
    
    private String title;
    private String guid;
    @Column(length = 10000)
    private String content;
}
