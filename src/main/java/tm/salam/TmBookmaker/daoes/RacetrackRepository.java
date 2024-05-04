package tm.salam.TmBookmaker.daoes;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.models.Racetrack;

import java.util.List;
import java.util.UUID;


@Repository
public interface RacetrackRepository extends JpaRepository<Racetrack, UUID> {

    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO racetrack(name, city_uuid, region_uuid) " +
            "VALUES(:name, :cityUuid, :regionUuid) ON CONFLICT DO NOTHING RETURNING TRUE")
    Boolean addRacetrack(@Param("name")String name, @Param("cityUuid")UUID cityUuid, @Param("regionUuid")UUID regionUuid);

    @Query(nativeQuery = true, value = "SELECT COUNT(racetrack) FROM racetrack " +
            "LEFT JOIN city ON (city.uuid = racetrack.city_uuid) " +
            "LEFT JOIN region ON (region.uuid = racetrack.region_uuid) " +
            "WHERE (LOWER(racetrack.name) LIKE CONCAT(:searchKey, '%')) OR " +
            "(LOWER(city.name) LIKE CONCAT(:searchKey, '%')) OR " +
            "(LOWER(region.name) LIKE CONCAT(:searchKey, '%'))")
    int getNumberRacetracksBySearchKey(@Param("searchKey")String searchKey);

    @Query(nativeQuery = true, value = "SELECT racetrack.uuid, racetrack.name, racetrack.city_uuid, racetrack.region_uuid, " +
            "racetrack.is_active, racetrack.created, racetrack.updated FROM racetrack " +
            "LEFT JOIN city ON (city.uuid = racetrack.city_uuid) " +
            "LEFT JOIN region ON (region.uuid = racetrack.region_uuid) " +
            "WHERE (LOWER(racetrack.name) LIKE CONCAT(:searchKey, '%')) OR " +
            "(LOWER(city.name) LIKE CONCAT(:searchKey, '%')) OR " +
            "(LOWER(region.name) LIKE CONCAT(:searchKey, '%'))")
    List<Racetrack> getRacetracksBySearchKey(Pageable pageable, @Param("searchKey")String searchKey);

    @Query("SELECT racetrack FROM Racetrack racetrack WHERE racetrack.uuid = :uuid")
    Racetrack getRacetrackByUuid(@Param("uuid")UUID uuid);

    @Transactional
    @Query(nativeQuery = true, value = "SELECT edit_racetrack(:uuid, TRIM(:name), :cityUuid, :regionUuid)")
    boolean editRacetrack(@Param("uuid")UUID uuid, @Param("name")String name, @Param("cityUuid")UUID cityUuid,
                          @Param("regionUuid")UUID regionUuid);

    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM racetrack WHERE racetrack.uuid = :uuid RETURNING TRUE")
    Boolean removeRacetrackByUuid(@Param("uuid")UUID uuid);

    @Query("SELECT NEW Racetrack(racetrack.uuid AS uuid, racetrack.name AS name) FROM Racetrack racetrack " +
            "WHERE racetrack.isActive = TRUE ORDER BY racetrack.name")
    List<Racetrack>getActiveRacetracks();

    @Transactional
    @Query(nativeQuery = true, value = "UPDATE racetrack SET is_active = :isActive WHERE racetrack.uuid = :uuid " +
            "RETURNING TRUE")
    Boolean switchActivationRacetrackByUuid(@Param("uuid")UUID uuid, @Param("isActive")boolean isActive);

}
