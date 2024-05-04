package tm.salam.TmBookmaker.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "horse_race")
public class HorseRace extends BaseEntity {

    @Column(name = "date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @Column(name = "time")
    @DateTimeFormat(pattern = "HH:mm")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime time;
    @Column(name = "number_horses")
    private int numberHorses;
    @Column(name = "number_bets")
    private int numberBets;
    @Column(name = "bet_permission")
    private boolean betPermission;
    @Column(name = "is_active")
    private boolean isActive;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "data_file_uuid", referencedColumnName = "uuid")
    private File dataFile;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "horse_race_event_uuid", referencedColumnName = "uuid")
    private HorseRaceEvent horseRaceEvent;
    @OneToMany(mappedBy = "horseRace", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Horse>horses;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "horse_race_bet_option",
            joinColumns = @JoinColumn(name = "horse_race_uuid", referencedColumnName = "uuid"),
            inverseJoinColumns = @JoinColumn(name = "bet_option_uuid", referencedColumnName = "uuid")
    )
    private List<BetOption>betOptions;
    @OneToMany(mappedBy = "horseRace", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Bet>bets;

}
