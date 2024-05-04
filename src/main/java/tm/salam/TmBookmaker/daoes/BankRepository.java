package tm.salam.TmBookmaker.daoes;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.models.Bank;

import java.util.List;
import java.util.UUID;

@Repository
public interface BankRepository extends JpaRepository<Bank, UUID> {

    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO bank(name) VALUES(:name) ON CONFLICT DO NOTHING RETURNING TRUE")
    Boolean addBank(@Param("name")String name);

    @Query(nativeQuery = true, value = "SELECT * FROM bank " +
            "WHERE LOWER(TRIM(bank.name)) LIKE CONCAT('%', :searchKey, '%') ORDER BY bank.name")
    List<Bank> getBanksBySearchKey(Pageable pageable, @Param("searchKey")String searchKey);

    @Transactional
    @Query("SELECT bank FROM Bank bank WHERE bank.uuid = :uuid")
    Bank getBankByUuid(@Param("uuid")UUID uuid);

    @Query(nativeQuery = true, value = "UPDATE bank SET name = :name WHERE bank.uuid = :uuid RETURNING TRUE")
    Boolean editBank(@Param("uuid")UUID uuid, @Param("name")String name);

    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM bank WHERE bank.uuid = :uuid RETURNING TRUE")
    Boolean removeBankByUuid(@Param("uuid")UUID uuid);

    @Query("SELECT NEW Bank(bank.uuid AS uuid, bank.name AS name) FROM Bank bank WHERE bank.isActive = TRUE")
    List<Bank>getActiveBanks();

}
