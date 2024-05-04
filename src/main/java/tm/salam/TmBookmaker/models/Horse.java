package tm.salam.TmBookmaker.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "horse")
public class Horse extends BaseEntity {

    @Column(name = "number")
    private String number;
    @Column(name = "is_active")
    @JsonProperty(value = "isActive")
    private boolean isActive;
    @Column(name = "place")
    private Integer place;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "horse_race_uuid", referencedColumnName = "uuid")
    private HorseRace horseRace;

    @Override
    public String toString() {
        return "Horse{" +
                "number='" + number + '\'' +
                ", isActive=" + isActive +
                '}';
    }

}
