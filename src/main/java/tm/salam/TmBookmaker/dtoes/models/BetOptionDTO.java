package tm.salam.TmBookmaker.dtoes.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BetOptionDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID uuid;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer odds;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal minAmount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal maxAmount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isActive;

}
