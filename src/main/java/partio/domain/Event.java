package partio.domain;

import partio.jsonconfig.EventDeserializer;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.hibernate.annotations.Proxy;
import org.springframework.data.jpa.domain.AbstractPersistable;
import partio.jsonconfig.EventSerializer;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@JsonDeserialize(using = EventDeserializer.class)
@JsonSerialize(using = EventSerializer.class)
//@Proxy(lazy=false)
//format is for reading date, serializer still has to write correct format
public class Event extends AbstractPersistable<Long> {
//event=kokous, sisältää aktiviteettejä

    private String title;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime endTime;

    private String type;
    @Column(length = 10000)
    private String information;

    @ManyToOne
    @JoinColumn
    private EventGroup groupId;

    //  @JsonManagedReference           //should be moved to buffer and not deleted anymore
    @OneToMany(mappedBy = "event", fetch = FetchType.EAGER/*, cascade = CascadeType.REMOVE, orphanRemoval = true*/)
    private List<Activity> activities;

    @ManyToOne
    @JoinColumn
    private Scout scout;

    public Event(String title, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, String type, String information, Scout scout) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
        this.information = information;
        this.groupId = null;
        this.activities = new ArrayList<>();
        this.scout = scout;
    }

    public void setVariables(Event event) {
        this.title = event.title;
        this.startDate = event.startDate;
        this.endDate = event.endDate;
        this.startTime = event.startTime;
        this.endTime = event.endTime;
        this.type = event.type;
        this.information = event.information;
    }

}
