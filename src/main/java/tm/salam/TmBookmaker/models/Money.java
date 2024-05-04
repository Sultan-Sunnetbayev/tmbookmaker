package tm.salam.TmBookmaker.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import tm.salam.TmBookmaker.helpers.types.TransactionStatus;
import tm.salam.TmBookmaker.helpers.types.TransactionType;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "money")
public class Money extends BaseEntity {

    @Column(name = "amount")
    @NotBlank(message = "error money amount should be")
    @NotNull(message = "error money amount don't be null")
    private BigDecimal amount;
    @Column(name = "date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    @Column(name = "time")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime time;
    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    @Column(name = "transaction_status")
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @OneToOne(mappedBy = "money", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private CashTransaction cashTransaction;
    @OneToOne(mappedBy = "money", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private CardTransaction cardTransaction;

}
