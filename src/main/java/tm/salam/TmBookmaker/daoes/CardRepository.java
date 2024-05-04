package tm.salam.TmBookmaker.daoes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.models.Card;

import java.util.List;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {

    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO card(card_number, holder_name, cvc_code, expiration_date, " +
            "bank_uuid, bettor_uuid) " +
            "VALUES(:cardNumber, :holderName, :cvcCode, :expirationDate, :bankUuid, :bettorUuid) " +
            "ON CONFLICT DO NOTHING RETURNING TRUE")
    Boolean addCard(@Param("cardNumber")String cardNumber, @Param("holderName")String holder, @Param("cvcCode")String cvcCode,
                    @Param("expirationDate")String expirationDate, @Param("bankUuid")UUID bankUuid,
                    @Param("bettorUuid")UUID bettorUuid);

    @Query(nativeQuery = true, value = "SELECT * FROM card WHERE card.bettor_uuid = :bettorUuid " +
            "ORDER BY card.created")
    List<Card> getCardsByBettorUuid(@Param("bettorUuid")UUID bettorUuid);

    @Transactional
    @Query(nativeQuery = true, value = "UPDATE card SET card_number = :cardNumber, holder_name = :holderName, " +
            "cvc_code = :cvcCode, expiration_date = :expirationDate, bank_uuid = :bankUuid WHERE card.uuid = :uuid " +
            "RETURNING TRUE")
    Boolean editCard(@Param("uuid")UUID uuid, @Param("cardNumber")String cardNumber, @Param("holderName")String holderName,
                     @Param("cvcCode")String cvcCode, @Param("expirationDate")String expirationDate,
                     @Param("bankUuid")UUID bankUuid);

    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM card WHERE card.uuid = :uuid RETURNING TRUE")
    Boolean removeCardByUuid(@Param("uuid")UUID uuid);

}
