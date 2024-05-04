package tm.salam.TmBookmaker.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "racetrack")
public class Racetrack extends BaseEntity {

    @Column(name = "name")
    @NotBlank(message = "error racetrack name should be")
    @NotNull(message = "error racetrack name don't be null")
    private String name;
    @Column(name = "is_active")
    private boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "city_uuid", referencedColumnName = "uuid")
    private City city;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "region_uuid", referencedColumnName = "uuid")
    private Region region;
    @OneToMany(mappedBy = "racetrack", fetch = FetchType.LAZY)
    private List<HorseRaceEvent>horseRaceEvents;


    public Racetrack(final UUID uuid, final String name){
        this.uuid=uuid;
        this.name=name;
    }

}
