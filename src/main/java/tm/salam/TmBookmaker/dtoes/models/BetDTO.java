package tm.salam.TmBookmaker.dtoes.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import tm.salam.TmBookmaker.helpers.types.BetStatusType;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BetDTO implements Comparable<BetDTO> {

    private UUID uuid;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BetOptionDTO betOptionDTO;
    private BetStatusType status;
    private MoneyDTO transactedMoney;

    @Override
    public int compareTo(BetDTO betDTO) {
        assert this.getBetOptionDTO()!=null && betDTO!=null;

        return this.getBetOptionDTO().getName().compareTo(betDTO.getBetOptionDTO().getName());
    }

}
