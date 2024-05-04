package tm.salam.TmBookmaker.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cash_register")
public class CashRegister extends BaseEntity {

    @Column(name = "number")
    private String number;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "is_removed")
    private boolean isRemoved;


    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "cashier_uuid", referencedColumnName = "uuid")
    private User cashier;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "populated_place_uuid", referencedColumnName = "uuid")
    private PopulatedPlace populatedPlace;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "city_uuid", referencedColumnName = "uuid")
    private City city;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "region_uuid", referencedColumnName = "uuid")
    private Region region;
    @OneToMany(mappedBy = "cashRegister", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CashTransaction> cashTransactions;

}
