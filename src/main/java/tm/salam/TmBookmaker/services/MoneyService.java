package tm.salam.TmBookmaker.services;

import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.models.Money;

import java.util.UUID;

public interface MoneyService {
    @Transactional
    ResponseTransfer<UUID> addMoney(Money money);
}
