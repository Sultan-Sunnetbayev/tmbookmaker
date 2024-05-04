package tm.salam.TmBookmaker.dtoes.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import tm.salam.TmBookmaker.helpers.types.TransactionStatus;
import tm.salam.TmBookmaker.helpers.types.TransactionType;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoneyDTO {

    private UUID uuid;
    private BigDecimal amount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalTime time;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date date;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TransactionType transactionType;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TransactionStatus transactionStatus;

    @Override
    public String toString() {
        return "MoneyDTO{" +
                "uuid=" + uuid +
                ", amount=" + amount +
                ", time=" + time +
                ", date=" + date +
                ", transactionType=" + transactionType +
                ", transactionStatus=" + transactionStatus +
                '}';
    }

}
