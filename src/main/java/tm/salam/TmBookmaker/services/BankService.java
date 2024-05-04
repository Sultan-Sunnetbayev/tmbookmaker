package tm.salam.TmBookmaker.services;

import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.dtoes.models.BankDTO;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.models.Bank;

import java.util.List;
import java.util.UUID;

public interface BankService {

    @Transactional
    ResponseTransfer<?> addBank(Bank bank);

    ResponseTransfer<List<BankDTO>>getBankDTOSBySearchKey(int page, int size, String searchKey);

    ResponseTransfer<BankDTO>getBankDTOByUuid(UUID uuid);

    @Transactional
    ResponseTransfer<?>editBank(Bank bank);

    @Transactional
    ResponseTransfer<?>removeBankByUuid(UUID uuid);

    ResponseTransfer<List<BankDTO>>getActiveBankDTOS();
}
