package tm.salam.TmBookmaker.dtoes.serializers;

import org.springframework.stereotype.Component;
import tm.salam.TmBookmaker.dtoes.models.PopulatedPlaceDTO;
import tm.salam.TmBookmaker.models.PopulatedPlace;

@Component
public class PopulatedPlaceDTOSerializer {

    private final CityDTOSerializer cityDTOSerializer;
    private final RegionDTOSerializer regionDTOSerializer;

    public PopulatedPlaceDTOSerializer(CityDTOSerializer cityDTOSerializer, RegionDTOSerializer regionDTOSerializer) {
        this.cityDTOSerializer = cityDTOSerializer;
        this.regionDTOSerializer = regionDTOSerializer;
    }

    public PopulatedPlaceDTO toPopulatedPlaceDTOGeneral(final PopulatedPlace populatedPlace){

        if(populatedPlace==null){

            return null;
        }

        return PopulatedPlaceDTO.builder()
                .uuid(populatedPlace.getUuid())
                .name(populatedPlace.getName())
                .cityDTO(cityDTOSerializer.toCityDTOOnlyName(populatedPlace.getCity()))
                .regionDTO(regionDTOSerializer.toRegionDTOOnlyName(populatedPlace.getRegion()))
                .isActive(populatedPlace.isActive())
                .build();
    }

    public PopulatedPlaceDTO toPopulatedPlaceDTO(final PopulatedPlace populatedPlace){

        if(populatedPlace==null){

            return null;
        }

        return PopulatedPlaceDTO.builder()
                .uuid(populatedPlace.getUuid())
                .name(populatedPlace.getName())
                .cityDTO(cityDTOSerializer.toCityDTOOnlyUuidAndName(populatedPlace.getCity()))
                .regionDTO(regionDTOSerializer.toRegionDTOOnlyUuidAndName(populatedPlace.getRegion()))
                .build();
    }

    public PopulatedPlaceDTO toPopulatedPlaceDTOOnlyName(final PopulatedPlace populatedPlace){

        if(populatedPlace==null){

            return null;
        }

        return PopulatedPlaceDTO.builder()
                .name(populatedPlace.getName())
                .build();
    }

    public PopulatedPlaceDTO toPopulatedPlaceDTOOnlyUuidAndName(final PopulatedPlace populatedPlace){

        if(populatedPlace==null){

            return null;
        }

        return PopulatedPlaceDTO.builder()
                .uuid(populatedPlace.getUuid())
                .name(populatedPlace.getName())
                .build();
    }

    public PopulatedPlaceDTO toPopulatedPlaceDTOOnlyUuidAndNameWithCityAndRegion(final PopulatedPlace populatedPlace){

        if(populatedPlace==null){

            return null;
        }

        return PopulatedPlaceDTO.builder()
                .uuid(populatedPlace.getUuid())
                .name(populatedPlace.getName())
                .cityDTO(cityDTOSerializer.toCityDTOOnlyUuidAndName(populatedPlace.getCity()))
                .regionDTO(regionDTOSerializer.toRegionDTOOnlyUuidAndName(populatedPlace.getRegion()))
                .build();
    }

}
