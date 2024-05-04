package tm.salam.TmBookmaker.daoes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.models.Money;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Repository
public interface MoneyRepository extends JpaRepository<Money, UUID> {

    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO money(amount, time, date, transaction_type, transaction_status) " +
            "VALUES(:amount, :time, :date, CAST(:transactionType AS TRANSACTIONTYPE), " +
            "CAST(:transactionStatus AS TRANSACTIONSTATUS)) RETURNING uuid")
    UUID addMoney(@Param("amount")BigDecimal amount, @Param("time")LocalTime time, @Param("date") LocalDate date,
                  @Param("transactionType")String transactionType,
                  @Param("transactionStatus")String transactionStatus);

}
