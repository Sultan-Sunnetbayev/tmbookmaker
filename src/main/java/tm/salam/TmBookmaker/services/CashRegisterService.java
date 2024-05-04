package tm.salam.TmBookmaker.services;

import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.dtoes.models.CashRegisterDTO;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.helpers.types.SortOption;
import tm.salam.TmBookmaker.models.CashRegister;

import java.util.List;
import java.util.UUID;

public interface CashRegisterService {

    @Transactional
    ResponseTransfer<?> addCashRegister(CashRegister cashRegister, UUID populatedPlaceUuid, UUID cityUuid,
                                        UUID regionUuid, UUID cashierRoleUuid);

    ResponseTransfer<Integer>getNumberCashRegistersBySearchKey(String searchKey);

    ResponseTransfer<List<CashRegisterDTO>>getCashRegisterDTOSBySearchKey(int page, int size,
                                                                          String searchKey, SortOption[] sortOptions);

    ResponseTransfer<CashRegisterDTO>getCashRegisterDTOByUuid(UUID uuid);

    ResponseTransfer<?>editCashRegister(CashRegister cashRegister, UUID populatedPlaceUuid,
                                        UUID cityUuid, UUID regionUuid);

    @Transactional
    ResponseTransfer<?>markCashRegisterAsRemoved(UUID cashRegisterUuid);

    @Transactional
    ResponseTransfer<?>switchActivationCashRegisterByUuid(UUID uuid, boolean isActive);
}
