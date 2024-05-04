package tm.salam.TmBookmaker.daoes;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.models.HorseRace;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface HorseRaceRepository extends JpaRepository<HorseRace, UUID> {

//    @Transactional
//    @Query(nativeQuery = true, value = "INSERT INTO horse_races(date, time, data_file_uuid, number_horses, " +
//            "horse_race_event_uuid) " +
//            "VALUES(:date, :time, :dataFileUuid, :numberHorses, :horseRaceEventUuid) ON CONFLICT DO NOTHING " +
//            "RETURNING CAST(uuid AS VARCHAR)")
//    UUID addHorseRace(@Param("date")Date date, @Param("time")LocalTime time, @Param("dataFileUuid")UUID dataFileUuid,
//                      @Param("numberHorses")int numberHorses, @Param("horseRaceEventUuid")UUID horseRaceEventUuid);

    @Query(nativeQuery = true, value = "SELECT add_horse_race(:time, :date, :dataFileUuid, :numberHorses, :betOptionUuids, " +
            ":horseRaceEventUuid)")
    UUID addHorseRace(@Param("date") LocalDate date, @Param("time")LocalTime time, @Param("dataFileUuid")UUID dataFileUuid,
                      @Param("numberHorses")int numberHorses, @Param("betOptionUuids")UUID[] betOptionUuids,
                      @Param("horseRaceEventUuid")UUID horseRaceEventUuid);

    @Query(nativeQuery = true, value = "SELECT COUNT(horse_race) FROM horse_race " +
            "WHERE (horse_race.is_active = :isActive) AND (horse_race.horse_race_event_uuid = :horseRaceEventUuid) AND " +
            "((CAST(horse_race.number_horses AS VARCHAR) LIKE CONCAT(:searchKey, '%')) OR " +
            "(CAST(horse_race.date AS VARCHAR) LIKE CONCAT('%', :searchKey, '%')) OR " +
            "(CAST(horse_race.time AS VARCHAR) LIKE CONCAT(:searchKey, '%')))")
    int getNumberHorseRacesBySearchKey(@Param("searchKey")String searchKey, @Param("isActive")boolean isActive,
                                       @Param("horseRaceEventUuid")UUID horseRaceEventUuid);

    @Query(nativeQuery = true, value = "SELECT * FROM horse_race " +
            "WHERE (horse_race.is_active = :isActive) AND (horse_race.horse_race_event_uuid = :horseRaceEventUuid) AND " +
            "((CAST(horse_race.number_horses AS VARCHAR) LIKE CONCAT(:searchKey, '%')) OR " +
            "(CAST(horse_race.date AS VARCHAR) LIKE CONCAT('%', :searchKey, '%')) OR " +
            "(CAST(horse_race.time AS VARCHAR) LIKE CONCAT(:searchKey, '%')))")
    List<HorseRace> getHorseRacesBySearchKey(Pageable pageable, @Param("searchKey")String searchKey,
                                             @Param("isActive")boolean isActive,
                                             @Param("horseRaceEventUuid")UUID horseRaceEventUuid);

    @Query("SELECT horseRace FROM HorseRace horseRace WHERE horseRace.uuid = :uuid")
    HorseRace getHorseRaceByUuid(@Param("uuid")UUID uuid);

    @Transactional
    @Query(nativeQuery = true, value = "UPDATE horse_race SET is_active = :isActive " +
            "WHERE horse_race.uuid = :horseRaceUuid RETURNING TRUE")
    Boolean switchHorseRaceActivation(@Param("horseRaceUuid")UUID horseRaceUuid, @Param("isActive")boolean isActive);

    @Transactional
    @Query(nativeQuery = true, value = "UPDATE horse_race SET bet_permission = :betPermission " +
            "WHERE horse_race.uuid = :horseRaceUuid RETURNING TRUE")
    Boolean switchHorseRaceBetPermission(@Param("horseRaceUuid")UUID horseRaceUuid,
                                         @Param("betPermission")boolean betPermission);

    @Query("SELECT horseRace FROM HorseRace horseRace WHERE horseRace.horseRaceEvent.uuid = :horseRaceEventUuid " +
            "ORDER BY horseRace.date, horseRace.time")
    List<HorseRace>getHorseRacesByHorseRaceEventUuid(@Param("horseRaceEventUuid")UUID horseRaceEventUuid);

    @Query("SELECT horseRace.isActive FROM HorseRace horseRace WHERE horseRace.uuid = :horseRaceUuid")
    Boolean isHorseRaceClosed(@Param("horseRaceUuid")UUID horseRaceUuid);

    @Query(nativeQuery = true, value = "SELECT CAST(horseRace.uuid AS VARCHAR) FROM horse_race " +
            "WHERE horse_race.horse_race_event_uuid = :horseRaceEventUuid")
    UUID[] getHorseRaceUuidsByHorseRaceEventUuid(@Param("horseRaceEventUuid")UUID horseRaceEventUuid);

}
