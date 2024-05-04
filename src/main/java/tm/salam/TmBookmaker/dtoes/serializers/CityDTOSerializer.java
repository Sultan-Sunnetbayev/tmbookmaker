package tm.salam.TmBookmaker.dtoes.serializers;

import org.springframework.stereotype.Component;
import tm.salam.TmBookmaker.dtoes.models.CityDTO;
import tm.salam.TmBookmaker.models.City;

@Component
public class CityDTOSerializer {

    private final RegionDTOSerializer regionDTOSerializer;

    public CityDTOSerializer(RegionDTOSerializer regionDTOSerializer) {
        this.regionDTOSerializer = regionDTOSerializer;
    }

    public CityDTO toCityDTOGeneral(final City city){

        if(city==null){
            return null;
        }

        return CityDTO.builder()
                .uuid(city.getUuid())
                .name(city.getName())
                .regionDTO(regionDTOSerializer.toRegionDTOOnlyName(city.getRegion()))
                .isActive(city.isActive())
                .build();
    }

    public CityDTO toCityDTO(final City city){

        if(city==null){
            return null;
        }

        return CityDTO.builder()
                .uuid(city.getUuid())
                .name(city.getName())
                .isActive(city.isActive())
                .regionDTO(regionDTOSerializer.toRegionDTOOnlyUuidAndName(city.getRegion()))
                .build();
    }

    public CityDTO toCityDTOOnlyUuidAndName(final City city){

        if(city==null){
            return null;
        }

        return CityDTO.builder()
                .uuid(city.getUuid())
                .name(city.getName())
                .build();
    }

    public CityDTO toCityDTOOnlyName(final City city){

        if(city==null){
            return null;
        }

        return CityDTO.builder()
                .name(city.getName())
                .build();
    }

    public CityDTO toCityDTOOnlyUuidAndNameWithRegion(final City city){

        if(city==null){
            return null;
        }

        return CityDTO.builder()
                .uuid(city.getUuid())
                .name(city.getName())
                .regionDTO(regionDTOSerializer.toRegionDTOOnlyUuidAndName(city.getRegion()))
                .build();
    }

}
