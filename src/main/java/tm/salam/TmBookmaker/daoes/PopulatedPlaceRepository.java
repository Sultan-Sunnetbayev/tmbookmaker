package tm.salam.TmBookmaker.daoes;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.models.PopulatedPlace;

import java.util.List;
import java.util.UUID;

@Repository
public interface PopulatedPlaceRepository extends JpaRepository<PopulatedPlace, UUID> {

    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO populated_place(name, city_uuid, region_uuid) " +
            "VALUES(TRIM(:name), :cityUuid, :regionUuid) ON CONFLICT DO NOTHING RETURNING TRUE")
    Boolean addPopulatedPlace(@Param("name")String name, @Param("cityUuid")UUID cityUuid,
                              @Param("regionUuid")UUID regionUuid);

    @Query(nativeQuery = true, value = "SELECT COUNT(populated_place) FROM populated_place " +
            "LEFT JOIN city ON (city.uuid = populated_place.city_uuid) " +
            "LEFT JOIN region ON (region.uuid = populated_place.region_uuid) " +
            "WHERE LOWER(populated_place.name) LIKE CONCAT(:searchKey, '%') OR " +
            "LOWER(city.name) LIKE CONCAT(:searchKey, '%') OR " +
            "LOWER(region.name) LIKE CONCAT(:searchKey, '%')")
    int getNumberPopulatedPlacesBySearchKey(@Param("searchKey")String searchKey);

    @Query(nativeQuery = true, value = "SELECT populated_place.uuid, populated_place.name, populated_place.is_active, " +
            "populated_place.city_uuid, populated_place.region_uuid, populated_place.created, populated_place.updated " +
            "FROM populated_places populated_place " +
            "LEFT JOIN city ON (city.uuid = populated_place.city_uuid) " +
            "LEFT JOIN region ON (region.uuid = populated_place.region_uuid) " +
            "WHERE LOWER(populated_place.name) LIKE CONCAT(:searchKey, '%') OR " +
            "LOWER(city.name) LIKE CONCAT(:searchKey, '%') OR " +
            "LOWER(region.name) LIKE CONCAT(:searchKey, '%')")
    List<PopulatedPlace> getPopulatedPlacesBySearchKey(Pageable pageable, @Param("searchKey")String searchKey);

    @Query("SELECT populatedPlace FROM PopulatedPlace populatedPlace WHERE populatedPlace.uuid = :uuid")
    PopulatedPlace getPopulatedPlaceByUuid(@Param("uuid")UUID uuid);

    @Transactional
    @Query(nativeQuery = true, value = "UPDATE populated_place SET name = TRIM(:name), city_uuid = :cityUuid, " +
            "region_uuid = :regionUuid WHERE populated_place.uuid = :uuid RETURNING TRUE")
    Boolean editPopulatedPlace(@Param("uuid")UUID uuid, @Param("name")String name, @Param("cityUuid")UUID cityUuid,
                               @Param("regionUuid")UUID regionUuid);

    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM populated_place WHERE populated_place.uuid = :uuid " +
            "RETURNING TRUE")
    Boolean removePopulatedPlaceByUuid(@Param("uuid")UUID uuid);

    @Transactional
    @Query(nativeQuery = true, value = "UPDATE populated_place SET is_active = :isActive " +
            "WHERE populated_place.uuid = :uuid RETURNING TRUE")
    Boolean switchActivationPopulatedPlaceByUuid(@Param("uuid")UUID uuid, @Param("isActive")boolean isActive);

    @Query(nativeQuery = true, value = "SELECT * FROM populated_place " +
            "WHERE (populated_place.is_active = TRUE) ORDER BY populated_place.name")
    List<PopulatedPlace>getActivePopulatedPlaces();

    @Query(nativeQuery = true, value = "SELECT * FROM populated_place " +
            "WHERE (populated_place.is_active = TRUE) AND (populated_place.city_uuid = :cityUuid) AND " +
            "(populated_place.region_uuid = :regionUuid) ORDER BY populated_place.name")
    List<PopulatedPlace>getActivePopulatedPlacesByCityUuidAndRegionUuid(@Param("cityUuid")UUID cityUuid,
                                                                        @Param("regionUuid")UUID regionUuid);

    @Query(nativeQuery = true, value = "SELECT * FROM populated_place " +
            "WHERE (populated_place.is_active = TRUE) AND (populated_place.city_uuid = :cityUuid) ORDER BY populated_place.name")
    List<PopulatedPlace>getActivePopulatedPlacesByCityUuid(@Param("cityUuid")UUID cityUuid);

    @Query(nativeQuery = true, value = "SELECT * FROM populated_place " +
            "WHERE (populated_place.is_active = TRUE) AND (populated_place.region_uuid = :regionUuid) ORDER BY populated_place.name")
    List<PopulatedPlace>getActivePopulatedPlacesRegionUuid(@Param("regionUuid")UUID regionUuid);

}
