package tm.salam.TmBookmaker.daoes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.models.BetHorse;

import java.util.UUID;

@Repository
public interface BetHorseRepository extends JpaRepository<BetHorse, UUID> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO bet_horse(bet_uuid, horse_uuid, place) " +
            "VALUES(:betUuid, :horseUuid, :place)")
    void addBetHorse(@Param("betUuid")UUID betUuid, @Param("horseUuid")UUID horseUuid, @Param("place")int place);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE bet_horse SET is_correct = CASE WHEN (bet_horse.place = :horsePlace) " +
            "THEN TRUE ELSE FALSE END WHERE bet_horse.horse_uuid = :horseUuid")
    void updateBetHorseState(@Param("horseUuid")UUID horseUuid, @Param("horsePlace")int horsePlace);

}
