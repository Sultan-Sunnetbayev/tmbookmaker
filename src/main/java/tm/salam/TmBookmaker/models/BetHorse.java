package tm.salam.TmBookmaker.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "bet_horse")
public class BetHorse extends BaseEntity{

    @Column(name = "place")
    private int place;
    @Column(name = "is_correct")
    private Boolean isCorrect;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "bet_uuid", referencedColumnName = "uuid")
    private Bet bet;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "horse_uuid", referencedColumnName = "uuid")
    private Horse horse;

}
