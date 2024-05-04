package tm.salam.TmBookmaker.daoes;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.models.HorseRaceEvent;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface HorseRaceEventRepository extends JpaRepository<HorseRaceEvent, UUID> {

    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO horse_race_event(name, time, date, racetrack_uuid) " +
            "VALUES(:name, :time, :date, :racetrackUuid) ON CONFLICT DO NOTHING RETURNING TRUE")
    Boolean addHorseRaceEvent(@Param("name")String name, @Param("time") LocalTime time, @Param("date")LocalDate date,
                              @Param("racetrackUuid")UUID racetrackUuid);

    @Query(nativeQuery = true, value = "SELECT COUNT(horse_race_event) FROM horse_race_event " +
            "WHERE (horse_race_event.is_active = :isActive) AND " +
            "(LOWER(horse_race_event.name) LIKE CONCAT('%', :searchKey, '%'))")
    int getNumberHorseRaceEventsBySearchKey(@Param("searchKey")String searchKey, @Param("isActive")boolean isActive);

    @Query(nativeQuery = true, value = "SELECT * FROM horse_race_event " +
            "WHERE (horse_race_event.is_active = :isActive) AND " +
            "(LOWER(horse_race_event.name) LIKE CONCAT('%', :searchKey, '%'))")
    List<HorseRaceEvent>getHorseRaceEventsBySearchKey(Pageable pageable, @Param("searchKey")String searchKey,
                                                       @Param("isActive")boolean isActive);

    @Query("SELECT horseRaceEvent FROM HorseRaceEvent horseRaceEvent WHERE horseRaceEvent.uuid = :uuid")
    HorseRaceEvent getHorseRaceEventByUuid(@Param("uuid")UUID uuid);

    @Transactional
    @Query(nativeQuery = true, value = "UPDATE horse_race_event " +
            "SET name = :name, time = :time, date = :date, racetrack_uuid = :racetrackUuid " +
            "WHERE horse_race_event.uuid = :uuid RETURNING TRUE")
    Boolean editHorseRaceEvent(@Param("uuid")UUID uuid, @Param("name")String name, @Param("time")LocalTime time,
                               @Param("date") LocalDate date, @Param("racetrackUuid")UUID racetrackUuid);

    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM horse_race_event WHERE horse_race_event.uuid = :uuid RETURNING TRUE")
    Boolean removeHorseRaceEventByUuid(@Param("uuid")UUID uuid);

    @Transactional
    @Query(nativeQuery = true, value = "UPDATE horse_race_event SET is_active = :isActive " +
            "WHERE horse_race_event.uuid = :uuid RETURNING TRUE")
    Boolean switchActivationHorseRaceEventByUuid(@Param("uuid")UUID uuid, @Param("isActive")boolean isActive);

    @Query("SELECT NEW HorseRaceEvent(horseRaceEvent.uuid AS uuid, horseRaceEvent.name AS name, " +
            "horseRaceEvent.time as time, horseRaceEvent.date AS date) FROM HorseRaceEvent horseRaceEvent " +
            "WHERE horseRaceEvent.isActive = TRUE ORDER BY horseRaceEvent.name")
    List<HorseRaceEvent>getActiveHorseRaceEvents();

    @Query("SELECT horseRaceEvent FROM HorseRaceEvent horseRaceEvent WHERE horseRaceEvent.eventNumber = 1")
    List<HorseRaceEvent>getLastHorseRaceEvents();

    @Query("SELECT horseRaceEvent FROM HorseRaceEvent horseRaceEvent WHERE horseRaceEvent.date = :date")
    List<HorseRaceEvent>getHorseRaceEventsByDate(@Param("date")LocalDate date);

}
