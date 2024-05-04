package tm.salam.TmBookmaker.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cash_transaction")
public class CashTransaction extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "bettor_uuid", referencedColumnName = "uuid")
    private Bettor bettor;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "money_uuid", referencedColumnName = "uuid")
    private Money money;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "cash_register_uuid", referencedColumnName = "uuid")
    private CashRegister cashRegister;

}
