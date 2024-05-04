package tm.salam.TmBookmaker.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "card")
public class Card extends BaseEntity{

    @Column(name = "card_number", nullable = false)
    @NotBlank(message = "error card number should be")
    @NotNull(message = "error card number don't be null")
    private String cardNumber;
    @Column(name = "holder_name")
    @NotBlank(message = "error holder name should be")
    @NotNull(message = "error holder name don't be null")
    private String holderName;
    @Column(name = "cvc_code")
    @NotBlank(message = "error cvc code card's should be")
    @NotNull(message = "error cvc code card's don't be null")
    private String cvcCode;
    @Column(name = "expiration_date")
    @NotBlank(message = "error expiration date card's should be")
    @NotNull(message = "error expiration date card's don't be null")
    private String expirationDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "bank_uuid", referencedColumnName = "uuid")
    private Bank bank;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "bettor_uuid", referencedColumnName = "uuid")
    private Bettor bettor;

}
