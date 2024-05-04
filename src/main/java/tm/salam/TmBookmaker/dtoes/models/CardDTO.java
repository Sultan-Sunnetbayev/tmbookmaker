package tm.salam.TmBookmaker.dtoes.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardDTO {

    private UUID uuid;
    private String cardNumber;
    private String holderName;
    private String cvcCode;
    private String expirationDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BankDTO bankDTO;

}
