package tm.salam.TmBookmaker.daoes;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.models.CashRegister;

import java.util.List;
import java.util.UUID;

@Repository
public interface CashRegisterRepository extends JpaRepository<CashRegister, UUID> {

    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO cash_register(number, cashier_uuid, populated_place_uuid, city_uuid, " +
            "region_uuid) VALUES(:number, :cashierUuid, :populatedPlaceUuid, :cityUuid, :regionUuid) " +
            "RETURNING TRUE")
    Boolean addCashRegister(@Param("number")String number, @Param("cashierUuid")UUID cashierUuid,
                            @Param("populatedPlaceUuid")UUID populatedPlaceUuid, @Param("cityUuid")UUID cityUuid,
                            @Param("regionUuid")UUID regionUuid);

    @Query(nativeQuery = true, value = "SELECT COUNT(cash_register) FROM cash_register " +
            "LEFT JOIN users userr ON (userr.uuid = cash_register.cashier_uuid) " +
            "LEFT JOIN populated_places populated_place ON (populated_place.uuid = cash_register.populated_place_uuid) " +
            "LEFT JOIN cities city ON (city.uuid = cash_register.city_uuid) " +
            "LEFT JOIN regions region ON (region.uuid = cash_register.region_uuid) " +
            "WHERE (cash_register.is_removed = FALSE) AND " +
            "((cash_register.number LIKE CONCAT (:searchKey, '%')) OR " +
            "(populated_place.name LIKE CONCAT (:searchKey, '%')) OR " +
            "(city.name LIKE CONCAT (:searchKey, '%')) OR " +
            "(region.name LIKE CONCAT (:searchKey, '%')))")
    int getNumberCashRegistersBySearchKey(@Param("searchKey")String searchKey);

    @Query(nativeQuery = true, value = "SELECT cash_register.uuid, cash_register.number, cash_register.is_active, " +
            "cash_register.cashier_uuid, cash_register.populated_place_uuid, cash_register.city_uuid, " +
            "cash_register.region_uuid, cash_register.is_removed, cash_register.created, cash_register.updated " +
            "FROM cash_register " +
            "LEFT JOIN user cashier ON (cashier.uuid = cash_register.cashier_uuid) " +
            "LEFT JOIN populated_place ON (populated_place.uuid = cash_register.populated_place_uuid) " +
            "LEFT JOIN cities city ON (city.uuid = cash_register.city_uuid) " +
            "LEFT JOIN region ON (region.uuid = cash_register.region_uuid) " +
            "WHERE (cash_register.is_removed = FALSE) AND " +
            "((cash_register.number LIKE CONCAT (:searchKey, '%')) OR " +
            "(populated_place.name LIKE CONCAT (:searchKey, '%')) OR " +
            "(city.name LIKE CONCAT (:searchKey, '%')) OR " +
            "(region.name LIKE CONCAT (:searchKey, '%')))")
    List<CashRegister> getCashRegistersBySearchKey(Pageable pageable, @Param("searchKey")String searchKey);

    @Query("SELECT cashRegister FROM CashRegister cashRegister WHERE cashRegister.uuid = :uuid")
    CashRegister getCashRegisterByUuid(@Param("uuid")UUID uuid);

    @Transactional
    @Query(nativeQuery = true, value = "UPDATE cash_register SET number = :number, " +
            "populated_place_uuid = :populatedPlaceUuid, city_uuid = :cityUuid, region_uuid = :regionUuid " +
            "WHERE cash_register.uuid = :uuid RETURNING TRUE")
    Boolean editCashRegister(@Param("uuid")UUID uuid, @Param("number")String number,
                             @Param("populatedPlaceUuid")UUID populatedPlaceUuid, @Param("cityUuid")UUID cityUuid,
                             @Param("regionUuid")UUID regionUuid);

    @Transactional
    @Query(nativeQuery = true, value = "UPDATE cash_register SET is_removed = TRUE " +
            "WHERE cash_register.uuid = :cashRegisterUuid RETURNING TRUE")
    Boolean markCashRegisterAsRemoved(@Param("cashRegisterUuid")UUID cashRegisterUuid);

    @Transactional
    @Query(nativeQuery = true, value = "UPDATE cash_register SET is_active = :isActive " +
            "WHERE cash_register.uuid = :uuid RETURNING TRUE")
    Boolean switchActivationCashRegisterByUuid(@Param("uuid")UUID uuid, @Param("isActive")boolean isActive);

}
