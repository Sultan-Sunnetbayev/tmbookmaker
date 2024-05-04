package tm.salam.TmBookmaker.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "bettor")
public class Bettor extends BaseEntity {

    @Column(name = "username")
    private String username;
    @Column(name = "phone_number")
    @NotBlank(message = "error bettor's phone number should be")
    @NotNull(message = "error bettor's phone number don't be null")
    private String phoneNumber;
    @Column(name = "password")
    private String password;
    @Column(name = "deposit")
    private BigDecimal deposit=setDefaultValueCash();
    @Column(name = "winnings")
    private BigDecimal winnings=setDefaultValueCash();
    @Column(name = "cash_out")
    private BigDecimal cashOut=setDefaultValueCash();
    @Column(name = "activation_code")
    private String activationCode;
    @Column(name = "limit_sms")
    private int limitSms;
    @Column(name = "confirm_age_18")
    @AssertTrue(message = "error bettor confirm age 18+")
    private Boolean confirmAge18;
    @Column(name = "acceptance_privacy_policy")
    @AssertTrue(message = "error bettor acceptance privacy policy")
    private Boolean acceptancePrivacyPolicy;
    @Column(name = "is_registered")
    private boolean isRegistered;
    @Column(name = "is_active")
    private boolean isActive;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "role_uuid", referencedColumnName = "uuid")
    private Role role;
    @OneToMany(mappedBy = "bettor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Card> cards;
    @OneToMany(mappedBy = "bettor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Bet>bets;
    @OneToMany(mappedBy = "bettor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CashTransaction>cashTransactions;


    private BigDecimal setDefaultValueCash(){

        return new BigDecimal("0.00");
    }

}
