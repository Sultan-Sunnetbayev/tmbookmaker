package tm.salam.TmBookmaker.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import tm.salam.TmBookmaker.dtoes.models.BankDTO;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "bank")
public class Bank extends BaseEntity{

    @Column(name = "name")
    @NotBlank(message = "error bank name should be")
    @NotNull(message = "error bank name don't be null")
    private String name;
    @Column(name = "is_active")
    private boolean isActive;

    @OneToMany(mappedBy = "bank", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Card> cards;

    public Bank(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

}
