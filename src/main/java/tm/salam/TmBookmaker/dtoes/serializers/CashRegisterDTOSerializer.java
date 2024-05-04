package tm.salam.TmBookmaker.dtoes.serializers;

import org.springframework.stereotype.Component;
import tm.salam.TmBookmaker.dtoes.models.CashRegisterDTO;
import tm.salam.TmBookmaker.models.CashRegister;

@Component
public class CashRegisterDTOSerializer {

    private final PopulatedPlaceDTOSerializer populatedPlaceDTOSerializer;
    private final CityDTOSerializer cityDTOSerializer;
    private final RegionDTOSerializer regionDTOSerializer;
    private final UserDTOSerializer userDTOSerializer;

    public CashRegisterDTOSerializer(PopulatedPlaceDTOSerializer populatedPlaceDTOSerializer, CityDTOSerializer cityDTOSerializer,
                                     RegionDTOSerializer regionDTOSerializer, UserDTOSerializer userDTOSerializer) {
        this.populatedPlaceDTOSerializer = populatedPlaceDTOSerializer;
        this.cityDTOSerializer = cityDTOSerializer;
        this.regionDTOSerializer = regionDTOSerializer;
        this.userDTOSerializer = userDTOSerializer;
    }

    public CashRegisterDTO toCashRegisterDTOGeneral(final CashRegister cashRegister){

        if(cashRegister==null){

            return null;
        }

        return CashRegisterDTO.builder()
                .uuid(cashRegister.getUuid())
                .number(cashRegister.getNumber())
                .isActive(cashRegister.isActive())
                .cashierDTO(userDTOSerializer.toGeneralCashierDTO(cashRegister.getCashier()))
                .populatedPlaceDTO(populatedPlaceDTOSerializer.toPopulatedPlaceDTOOnlyName(cashRegister.getPopulatedPlace()))
                .cityDTO(cityDTOSerializer.toCityDTOOnlyName(cashRegister.getCity()))
                .regionDTO(regionDTOSerializer.toRegionDTOOnlyName(cashRegister.getRegion()))
                .build();
    }

    public CashRegisterDTO toCashRegisterDTO(final CashRegister cashRegister){

        if(cashRegister==null){

            return null;
        }

        return CashRegisterDTO.builder()
                .uuid(cashRegister.getUuid())
                .number(cashRegister.getNumber())
                .cashierDTO(userDTOSerializer.toCashierDTO(cashRegister.getCashier()))
                .populatedPlaceDTO(populatedPlaceDTOSerializer.toPopulatedPlaceDTOOnlyUuidAndName(cashRegister.getPopulatedPlace()))
                .cityDTO(cityDTOSerializer.toCityDTOOnlyUuidAndName(cashRegister.getCity()))
                .regionDTO(regionDTOSerializer.toRegionDTOOnlyUuidAndName(cashRegister.getRegion()))
                .build();
    }

}
