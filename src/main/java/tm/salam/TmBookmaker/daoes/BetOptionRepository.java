package tm.salam.TmBookmaker.daoes;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.models.BetOption;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface BetOptionRepository extends JpaRepository<BetOption, UUID> {

    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO bet_option(name, min_amount, max_amount) " +
            "VALUES(TRIM(:name), :minAmount, :maxAmount) ON CONFLICT DO NOTHING RETURNING TRUE")
    Boolean addBetOption(@Param("name")String name, @Param("minAmount")BigDecimal minAmount,
                         @Param("maxAmount")BigDecimal maxAmount);

    @Query(nativeQuery = true, value = "SELECT COUNT(bet_option) FROM bet_option " +
            "WHERE LOWER(bet_option.name) LIKE CONCAT(:searchKey, '%')")
    int getNumberBetOptionsBySearchKey(@Param("searchKey")String searchKey);

    @Query(nativeQuery = true, value = "SELECT * FROM bet_option " +
            "WHERE LOWER(bet_option.name) LIKE CONCAT(:searchKey, '%')")
    List<BetOption> getBetOptionsBySearchKey(Pageable pageable, @Param("searchKey")String searchKey);

    @Query("SELECT betOption FROM BetOption betOption WHERE betOption.uuid = :uuid")
    BetOption getBetOptionByUuid(@Param("uuid")UUID uuid);

    @Transactional
    @Query(nativeQuery = true, value = "SELECT edit_bet_option(:uuid, :name, :minAmount, :maxAmount)")
    boolean editBetOption(@Param("uuid")UUID uuid, @Param("name")String name, @Param("minAmount")BigDecimal minAmount,
                          @Param("maxAmount")BigDecimal maxAmount);

    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM bet_option WHERE bet_option.uuid = :uuid RETURNING TRUE")
    Boolean removeBetOptionByUuid(@Param("uuid")UUID uuid);

    @Transactional
    @Query(nativeQuery = true, value = "UPDATE bet_option SET is_active = :isActive " +
            "WHERE bet_option.uuid = :uuid RETURNING TRUE")
    Boolean switchActivationBetOptionByUuid(@Param("uuid")UUID uuid, @Param("isActive")boolean isActive);

    @Query("SELECT NEW BetOption(betOption.uuid AS uuid, betOption.name AS name) FROM BetOption betOption " +
            "WHERE (betOption.isActive = TRUE) AND (betOption.odds IS NOT NULL) ORDER BY betOption.name")
    List<BetOption>getActiveBetOptions();

}
