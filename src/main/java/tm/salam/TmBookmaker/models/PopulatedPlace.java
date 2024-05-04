package tm.salam.TmBookmaker.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "populated_place")
public class PopulatedPlace extends BaseEntity {

    @Column(name = "name")
    @NotNull(message = "populated place name don't be null")
    @NotEmpty(message = "populated place name don't be empty")
    @Size(min = 1, max = 126, message = "city name length should be long than 0 and less than 126")
    private String name;
    @Column(name = "is_active")
    private boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "region_uuid", referencedColumnName = "uuid")
    private Region region;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "city_uuid", referencedColumnName = "uuid")
    private City city;
    @OneToMany(mappedBy = "populatedPlace", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CashRegister> cashRegisters;

}
