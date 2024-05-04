package tm.salam.TmBookmaker.daoes;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.models.Region;

import java.util.List;
import java.util.UUID;

@Repository
public interface RegionRepository extends JpaRepository<Region, UUID> {

    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO region(name) VALUES(TRIM(:name)) ON CONFLICT DO NOTHING " +
            "RETURNING TRUE")
    Boolean addRegion(@Param("name")String name);

    @Query(nativeQuery = true, value = "SELECT COUNT(region) FROM region " +
            "WHERE (LOWER(region.name) LIKE CONCAT(:searchKey, '%'))")
    int getNumberRegionsBySearchKey(@Param("searchKey")String searchKey);

    @Query(nativeQuery = true, value = "SELECT * FROM region " +
            "WHERE (LOWER(region.name) LIKE CONCAT(:searchKey, '%'))")
    List<Region>getRegionsBySearchKey(Pageable pageable, @Param("searchKey")String searchKey);

    @Query("SELECT NEW Region(region.uuid AS uuid, region.name AS name, region.isActive AS isActive) FROM Region region " +
            "WHERE region.uuid = :regionUuid")
    Region getRegionByUuid(@Param("regionUuid")UUID regionUuid);

    @Transactional
    @Query(nativeQuery = true, value = "SELECT edit_region(:uuid, TRIM(:name))")
    Boolean editRegion(@Param("uuid")UUID uuid, @Param("name")String name);

    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM region WHERE region.uuid = :uuid RETURNING TRUE")
    Boolean removeRegionByUuid(@Param("uuid")UUID uuid);

    @Query("SELECT NEW Region(region.uuid AS uuid, region.name AS name) FROM Region region WHERE region.isActive = TRUE " +
            "ORDER BY region.name")
    List<Region>getActiveRegions();

    @Transactional
    @Query(nativeQuery = true, value = "UPDATE region SET is_active = :isActive WHERE region.uuid = :uuid " +
            "RETURNING TRUE")
    Boolean switchActivationRegionByUuid(@Param("uuid")UUID uuid, @Param("isActive")boolean isActive);

}
