package tm.salam.TmBookmaker.dtoes.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegionDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID uuid;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isActive;

    @Override
    public String toString() {
        return "RegionDTO{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                '}';
    }

}
