package tm.salam.TmBookmaker.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "role")
public class Role extends BaseEntity{

    @Column(name = "name")
    @NotBlank(message = "error role name should be")
    @NotEmpty(message = "error role name don't be null")
    private String name;

}
