package tm.salam.TmBookmaker.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "horse_race_event")
public class HorseRaceEvent extends BaseEntity {

    @Column(name = "name")
    private String name;
    @Column(name = "time")
    @DateTimeFormat(pattern = "HH:mm")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime time;
    @Column(name = "date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @Column(name = "event_number")
    private int eventNumber;
    @Column(name = "number_horse_races")
    private int numberHorseRaces;
    @Column(name = "number_bets")
    private int numberBets;
    @Column(name = "is_active")
    private boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "racetrack_uuid", referencedColumnName = "uuid")
    private Racetrack racetrack;

    public HorseRaceEvent(UUID uuid, String name, LocalTime time, LocalDate date) {
        this.uuid = uuid;
        this.name = name;
        this.time = time;
        this.date = date;
    }

}
