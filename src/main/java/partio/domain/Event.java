package partio.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
//import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Event extends AbstractPersistable<Long> {

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;  
    @Column(length = 10000)
    private String description;
/*
    For example:
    @ManyToMany
    private List<Scout> leaders;
    @ManyToMany
    private List<Scout> scouts;
    */
}
