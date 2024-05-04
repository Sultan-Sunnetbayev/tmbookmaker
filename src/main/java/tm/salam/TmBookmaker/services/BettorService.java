package tm.salam.TmBookmaker.services;

import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.dtoes.models.BettorDTO;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.helpers.types.SortOption;
import tm.salam.TmBookmaker.models.Bettor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface BettorService {

    @Transactional
    ResponseTransfer<?>registrateBettor(Bettor bettor, UUID roleUuid);

    @Transactional
    ResponseTransfer<UUID>confirmBettorByActivationCode(Bettor bettor);

    @Transactional
    ResponseTransfer<?>activateBettor(Bettor bettor);

    ResponseTransfer<Integer> getNumberBettorsBySearchKey(String searchKey);

    ResponseTransfer<List<BettorDTO>>getBettorDTOSBySearchKey(int page, int size, String searchKey,
                                                              SortOption[] sortOptions);

    ResponseTransfer<Map<String, String>>getFinanceStateBettors(String searchKey);

    ResponseTransfer<BettorDTO>getBettorDTOByUuid(UUID uuid);

    @Transactional
    ResponseTransfer<?>editBettor(Bettor bettor);

    @Transactional
    ResponseTransfer<?>removeBettorByUuid(UUID uuid);

    @Transactional
    ResponseTransfer<?> topUpDeposit(Bettor bettor);

    ResponseTransfer<BettorDTO>getBettorDTOProfileByUuidForAppClient(UUID uuid);

    @Transactional
    ResponseTransfer<?>deactivateBettorByUuid(UUID uuid);

    ResponseTransfer<?>checkDepositValidityForPlacementBets(UUID bettorUuid, BigDecimal betAmount);
}
