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
@Table(name = "region")
public class Region extends BaseEntity {

    @Column(name = "name")
    @NotNull(message = "region name don't be null")
    @NotEmpty(message = "region name don't be empty")
    @Size(min = 1, max = 20, message = "region name length should be long than 0 and less than 20")
    private String name;
    @Column(name = "is_active")
    private boolean isActive;

    @OneToMany(mappedBy = "region",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<City>cities;
    @OneToMany(mappedBy = "region",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PopulatedPlace>populatedPlaces;
    @OneToMany(mappedBy = "region", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Racetrack>racetracks;
    @OneToMany(mappedBy = "region", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CashRegister>cashRegisters;

    public Region(final UUID uuid, final String name){
        this.uuid=uuid;
        this.name=name;
    }

    public Region(final UUID uuid, final String name, final boolean isActive){
        this.uuid=uuid;
        this.name=name;
        this.isActive=isActive;
    }

}
