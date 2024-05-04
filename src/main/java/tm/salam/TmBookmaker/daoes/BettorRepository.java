package tm.salam.TmBookmaker.daoes;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.models.Bettor;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface BettorRepository extends JpaRepository<Bettor, UUID> {

    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO " +
            "bettor(phone_number, password, role_uuid, limit_sms, activation_code, confirm_age_18, " +
            "acceptance_privacy_policy, updated) " +
            "VALUES(:phoneNumber, :password, :roleUuid, :limitSms, :activationCode, :confirmAge18, " +
            ":acceptancePrivacyPolicy, :updated) " +
            "ON CONFLICT(phone_number) DO UPDATE SET " +
            "limit_sms = CASE WHEN bettors.updated < NOW() - INTERVAL '1 HOUR' THEN EXCLUDED.limit_sms " +
            "            ELSE bettors.limit_sms END, " +
            "activation_code = EXCLUDED.activation_code, " +
            "confirm_age_18 = EXCLUDED.confirm_age_18, " +
            "acceptance_privacy_policy = EXCLUDED.acceptance_privacy_policy, " +
            "updated = EXCLUDED.updated " +
            "RETURNING limit_sms")
    int registrateBettor(@Param("phoneNumber")String phoneNumber, @Param("password")String password,
                         @Param("roleUuid")UUID roleUuid, @Param("limitSms")int limitSms,
                         @Param("activationCode")String activationCode, @Param("confirmAge18")boolean confirmAge18,
                         @Param("acceptancePrivacyPolicy")boolean acceptancePrivacyPolicy, @Param("updated")Date updated);

    @Modifying
    @Transactional
    @Query("UPDATE Bettor bettor SET bettor.limitSms = bettor.limitSms - :value " +
            "WHERE bettor.phoneNumber = :phoneNumber")
    void decrementBettorLimitSms(@Param("phoneNumber")String phoneNumber, @Param("value")int value);

    @Transactional
    @Query(nativeQuery = true, value = "UPDATE bettor SET activation_code = NULL " +
            "WHERE (bettor.phone_number = :phoneNumber) AND (bettor.activation_code = :activationCode) AND " +
            "(bettor.updated > NOW() - CAST(:lifeTimeActivationCode AS INTERVAL)) RETURNING CAST(bettor.uuid AS VARCHAR)")
    UUID confirmBettorByActivationCode(@Param("phoneNumber")String phoneNumber,
                                       @Param("activationCode")String activationCode,
                                       @Param("lifeTimeActivationCode")String lifeTimeActivationCode);

    @Transactional
    @Query(nativeQuery = true, value = "UPDATE bettor SET username = TRIM(:username), is_active = TRUE, " +
            "is_registered = TRUE WHERE (bettor.uuid = :uuid) AND (bettor.activation_code  IS NULL) RETURNING TRUE")
    Boolean activateBettor(@Param("uuid")UUID uuid, @Param("username")String username);

    @Query(nativeQuery = true, value = "SELECT COUNT(bettor) FROM bettor " +
            "WHERE (LOWER(bettor.username) LIKE CONCAT('%', :searchKey, '%')) AND (bettor.is_active=TRUE)")
    int getNumberBettorsBySearchKey(@Param("searchKey")String searchKey);

    @Query(nativeQuery = true, value = "SELECT * FROM bettor " +
            "WHERE (LOWER(TRIM(bettor.username)) LIKE CONCAT('%', :searchKey, '%')) AND (bettor.is_active=TRUE)")
    List<Bettor> getBettorsBySearchKey(Pageable pageable, @Param("searchKey")String searchKey);

    @Query("SELECT bettor FROM Bettor bettor WHERE bettor.uuid = :uuid")
    Bettor getBettorByUuid(@Param("uuid")UUID uuid);

    @Query(nativeQuery = true, value = "SELECT CASE WHEN COUNT(bettor)>0 THEN TRUE ELSE FALSE END FROM bettor " +
            "WHERE (bettor.uuid <> :uuid) AND (bettor.phone_number = :phoneNumber)")
    boolean isBettorExistsByPhoneNumber(@Param("uuid")UUID uuid, @Param("phoneNumber")String phoneNumber);

    @Transactional
    @Query(nativeQuery = true, value = "UPDATE bettor SET username = :username, phone_number = :phoneNumber, " +
            "deposit = :deposit WHERE bettor.uuid = :uuid RETURNING TRUE")
    Boolean editBettor(@Param("uuid")UUID uuid, @Param("username")String username, @Param("phoneNumber")String phoneNumber,
                       @Param("deposit")BigDecimal deposit);

    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM bettor WHERE bettor.uuid = :uuid RETURNING TRUE")
    Boolean removeBettorByUuid(@Param("uuid")UUID uuid);

    @Query(nativeQuery = true, value = "SELECT ARRAY[SUM(bettor.deposit), SUM(bettor.winnings), " +
            "SUM(bettor.cash_out)] FROM bettor " +
            "WHERE (bettor.is_active = TRUE) AND (LOWER(TRIM(bettor.username)) LIKE CONCAT(:searchKey, '%'))")
    String getFinanceStateBettors(@Param("searchKey")String searchKey);

    @Transactional
    @Query(nativeQuery = true, value = "UPDATE bettor SET deposit = deposit + :deposit " +
            "WHERE bettor.uuid = :uuid RETURNING TRUE")
    Boolean topUpDeposit(@Param("uuid")UUID uuid, @Param("deposit")BigDecimal deposit);

    @Query("SELECT bettor FROM Bettor bettor WHERE bettor.phoneNumber = :phoneNumber")
    Bettor getBettorByPhoneNumber(@Param("phoneNumber")String phoneNumber);

    @Transactional
    @Query(nativeQuery = true, value = "UPDATE bettor SET is_active = FALSE WHERE bettor.uuid = :uuid " +
            "RETURNING TRUE")
    Boolean deactivateBettorByUuid(@Param("uuid")UUID uuid);

    @Query("SELECT CASE WHEN bettor.deposit >= :betAmount THEN TRUE ELSE FALSE END FROM Bettor bettor " +
            "WHERE bettor.uuid = :uuid")
    boolean checkDepositValidityForBets(@Param("uuid")UUID uuid, @Param("betAmount")BigDecimal betAmount);

    @Transactional
    @Modifying
    @Query("UPDATE Bettor bettor SET bettor.deposit = bettor.deposit - :betAmount WHERE bettor.uuid = :uuid")
    void decrementDepositOnBetPlacementByBettorUuid(@Param("uuid")UUID uuid, @Param("betAmount")BigDecimal betAmount);

}
