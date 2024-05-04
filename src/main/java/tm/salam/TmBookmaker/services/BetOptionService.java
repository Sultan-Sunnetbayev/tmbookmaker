package tm.salam.TmBookmaker.services;

import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.dtoes.models.BetOptionDTO;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.helpers.types.SortOption;
import tm.salam.TmBookmaker.models.BetOption;

import java.util.List;
import java.util.UUID;

public interface BetOptionService {

    @Transactional
    ResponseTransfer<?> addBetOption(BetOption betOption);

    ResponseTransfer<Integer>getNumberBetOptionsBySearchKey(String searchKey);

    ResponseTransfer<List<BetOptionDTO>>getBetOptionDTOSBySearchKey(int page, int size, String searchKey,
                                                                   SortOption[] sortOptions);

    ResponseTransfer<BetOptionDTO>getBetOptionDTOByUuid(UUID uuid);

    @Transactional
    ResponseTransfer<?>editBetOption(BetOption betOption);

    @Transactional
    ResponseTransfer<?>removeBetOptionByUuid(UUID uuid);

    @Transactional
    ResponseTransfer<?>switchActivationBetOptionByUuid(UUID uuid, boolean isActive);

    ResponseTransfer<List<BetOptionDTO>>getActiveBetOptionDTOS();
}
