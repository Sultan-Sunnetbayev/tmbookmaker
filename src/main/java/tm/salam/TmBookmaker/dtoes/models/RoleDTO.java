package tm.salam.TmBookmaker.dtoes.models;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDTO {

    private UUID uuid;
    private String name;

}
