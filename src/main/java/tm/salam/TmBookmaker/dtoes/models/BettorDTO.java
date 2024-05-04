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
public class BettorDTO {

    private UUID uuid;
    private String username;
    private String phoneNumber;
    private BigDecimal deposit;
    private BigDecimal winnings;
    private BigDecimal cashOut;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isRegistered;

}
