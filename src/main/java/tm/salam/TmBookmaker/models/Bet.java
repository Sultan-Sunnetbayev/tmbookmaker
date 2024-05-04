package tm.salam.TmBookmaker.models;

import jakarta.persistence.*;
import lombok.*;
import tm.salam.TmBookmaker.helpers.types.BetStatusType;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "bet")
public class Bet extends BaseEntity {

    @Column(name = "odds")
    private int odds;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BetStatusType status;
    @Column(name = "winnings")
    private BigDecimal winnings;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "bet_option_uuid", referencedColumnName = "uuid")
    private BetOption betOption;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "bettor_uuid", referencedColumnName = "uuid")
    private Bettor bettor;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "horse_race_uuid", referencedColumnName = "uuid")
    private HorseRace horseRace;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "transacted_money_uuid", referencedColumnName = "uuid")
    private Money transactedMoney;
    @OneToMany(mappedBy = "bet", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BetHorse> betHorses;

}
