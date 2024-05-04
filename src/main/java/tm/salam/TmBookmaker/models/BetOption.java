package tm.salam.TmBookmaker.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "bet_option")
public class BetOption extends BaseEntity {

    @Column(name = "name")
    private String name;
    @Column(name = "odds")
    private Integer odds;
    @Column(name = "min_amount")
    private BigDecimal minAmount;
    @Column(name = "max_amount")
    private BigDecimal maxAmount;
    @Column(name = "is_active")
    private boolean isActive;

    public BetOption(final UUID uuid, final String name){
        this.uuid=uuid;
        this.name=name;
    }

}
