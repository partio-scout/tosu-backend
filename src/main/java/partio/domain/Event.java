package partio.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
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

    private String title;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;  
    private LocalTime endTime; 
    private String type;
    @Column(length = 10000)
    private String information;
    private List<String> activities;
    
/*
    For example:
    @ManyToMany
    private List<Scout> leaders;
    @ManyToMany
    private List<Scout> scouts;
    */


}
