package tm.salam.TmBookmaker.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "city")
public class City extends BaseEntity {

    @Column(name = "name")
    @NotNull(message = "city name don't be null")
    @NotEmpty(message = "city name don't be empty")
    @Size(min = 1, max = 55, message = "city name length should be long than 0 and less than 56")
    private String name;
    @Column(name = "is_active")
    private boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "region_uuid", referencedColumnName = "uuid")
    private Region region;
    @OneToMany(mappedBy = "city", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PopulatedPlace>populatedPlaces;
    @OneToMany(mappedBy = "city", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Racetrack>racetracks;
    @OneToMany(mappedBy = "city", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CashRegister>cashRegisters;

    public City(final UUID uuid, final String name){
        this.uuid=uuid;
        this.name=name;
    }

}
