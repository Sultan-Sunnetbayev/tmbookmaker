package tm.salam.TmBookmaker.daoes;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.models.City;

import java.util.List;
import java.util.UUID;

@Repository
public interface CityRepository extends JpaRepository<City, UUID> {

    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO city(name, region_uuid) VALUES(TRIM(:name), :regionUuid) " +
            "ON CONFLICT DO NOTHING RETURNING TRUE")
    Boolean addCity(@Param("name")String name, @Param("regionUuid")UUID regionUuid);

    @Query(nativeQuery = true, value = "SELECT COUNT(city) FROM city " +
            "LEFT JOIN region ON(region.uuid = city.region_uuid) " +
            "WHERE (LOWER(city.name) LIKE CONCAT(:searchKey, '%')) OR " +
            "(LOWER(region.name) LIKE CONCAT(:searchKey, '%'))")
    int getNumberCitiesBySearchKey(@Param("searchKey")String searchKey);

    @Query(nativeQuery = true, value = "SELECT city.uuid, city.name, city.is_active, city.region_uuid, city.created, " +
            "city.updated FROM city " +
            "LEFT JOIN region ON(region.uuid = city.region_uuid) " +
            "WHERE (LOWER(city.name) LIKE CONCAT(:searchKey, '%')) OR " +
            "(LOWER(region.name) LIKE CONCAT(:searchKey, '%'))")
    List<City> getCitiesBySearchKey(Pageable pageable, @Param("searchKey")String searchKey);

    @Query("SELECT city FROM City city WHERE city.uuid = :uuid")
    City getCityByUuid(@Param("uuid")UUID uuid);

    @Transactional
    @Query(nativeQuery = true, value = "SELECT edit_city(:uuid, TRIM(:name), :regionUuid)")
    Boolean editCity(@Param("uuid")UUID uuid, @Param("name")String name, @Param("regionUuid")UUID regionUuid);

    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM cities city WHERE city.uuid = :uuid RETURNING TRUE")
    Boolean removeCityByUuid(@Param("uuid")UUID uuid);

    @Query("SELECT city FROM City city WHERE city.region.uuid = :regionUuid ORDER BY city.name")
    List<City>getCitiesByRegionUuid(@Param("regionUuid")UUID regionUuid);

    @Query("SELECT city FROM City city WHERE city.isActive = TRUE ORDER BY city.name")
    List<City>getActiveCities();

    @Transactional
    @Query(nativeQuery = true, value = "UPDATE city SET is_active = :isActive WHERE city.uuid = :uuid " +
            "RETURNING TRUE")
    Boolean switchActivationCityByUuid(@Param("uuid")UUID uuid, @Param("isActive")boolean isActive);

}
