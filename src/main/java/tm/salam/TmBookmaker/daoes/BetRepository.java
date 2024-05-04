package tm.salam.TmBookmaker.daoes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.models.Bet;

import java.util.List;
import java.util.UUID;

@Repository
public interface BetRepository extends JpaRepository<Bet, UUID> {

    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO bet(bet_option_uuid, horse_race_uuid, bettor_uuid, " +
            "transacted_money_uuid, odds, status) VALUES(:betOptionUuid, :horseRaceUuid, :bettorUuid, " +
            ":transactedMoneyUuid, :odds, CAST(:status AS BETSTATUSTYPE)) RETURNING CAST(uuid AS VARCHAR)")
    UUID addBet(@Param("betOptionUuid")UUID betOptionUuid,
                @Param("horseRaceUuid")UUID horseRaceUuid,
                @Param("bettorUuid")UUID bettorUuid,
                @Param("transactedMoneyUuid")UUID transactedMoneyUuid,
                @Param("odds")int odds,
                @Param("status")String status);

    @Query("SELECT bet FROM Bet bet WHERE (bet.bettor.uuid = :bettorUuid) AND (bet.horseRace.uuid = :horseRaceUuid) " +
            "ORDER BY bet.odds")
    Bet[] getBetsByBettorUuidAndHorseRaceUuid(@Param("bettorUuid")UUID bettorUuid,
                                              @Param("horseRaceUuid")UUID horseRaceUuid);

    @Query("SELECT bet FROM Bet bet WHERE (bet.bettor.uuid = :bettorUuid) AND (bet.horseRace.uuid IN :horseRaceUuids) " +
            "ORDER BY bet.odds")
    Bet[] getBetsByBettorUuidAndHorseRaceUuids(@Param("bettorUuid")UUID bettorUuid,
                                               @Param("horseRaceUuids") List<UUID>horseRaceUuids);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE bet SET status = CASE WHEN get_final_result_bet_horses(bet.uuid) " +
            "THEN CAST('WIN' AS BETSTATUSTYPE) ELSE CAST('LOSE' AS BETSTATUSTYPE) END WHERE bet.horse_race_uuid = :horseRaceUuid")
    void updateBetStatus(@Param("horseRaceUuid")UUID horseRaceUuid);

}
