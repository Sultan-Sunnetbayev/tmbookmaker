package tm.salam.TmBookmaker.daoes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.models.Horse;

import java.util.List;
import java.util.UUID;

@Repository
public interface HorseRepository extends JpaRepository<Horse, UUID> {

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO horse(number, is_active, horse_race_uuid) " +
            "VALUES(:number, :isActive, :horseRaceUuid)")
    void addHorse(@Param("number")String number, @Param("isActive")boolean isActive,
                  @Param("horseRaceUuid")UUID horseRaceUuid);


//    @Transactional
//    @Modifying
//    @Query(nativeQuery = true, value = "UPDATE horses horse SET place = :place WHERE horse.uuid = :uuid")
//    void addHorseRaceResult(@Param("uuid")UUID uuid, @Param("place")int place);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE horse SET place = :place WHERE horse.uuid = :uuid")
    void updateHorsePlace(@Param("uuid")UUID uuid, @Param("place") int place);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE horse SET is_active = :isActive WHERE horse.uuid = :uuid")
    void switchActivationHorse(@Param("uuid")UUID uuid, @Param("isActive")boolean isActive);

    @Query(nativeQuery = true, value = "SELECT horse.uuid, horse.number, horse.is_active, horse.place, horse.horse_race_uuid, " +
            "horse.created, horse.updated FROM horse WHERE horse.horse_race_uuid = :horseRaceUuid")
    List<Horse>getHorsesByHorseRaceUuid(@Param("horseRaceUuid")UUID horseRaceUuid);

}
