package partio.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import org.springframework.data.jpa.domain.AbstractPersistable;
//import javax.persistence.ManyToMany;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class Event extends AbstractPersistable<Long> {
//event=kokous, sisältää aktiviteettejä
    private String title;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private LocalDate endDate;  
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime endTime; 
    
    private String type;
    @Column(length = 10000)
    private String information;
   
    @JsonManagedReference
    @OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Activity> activities;
    
    public void setAllButId(Event event) {
        this.title = event.title;
        this.startDate = event.startDate;
        this.endDate = event.endDate;
        this.startTime = event.startTime;
        this.endTime = event.endTime;
        this.type = event.type;
        this.information = event.information;
        
        this.activities = event.activities;
    }
    
}
