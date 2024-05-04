package tm.salam.TmBookmaker.dtoes.serializers;

import org.springframework.stereotype.Component;
import tm.salam.TmBookmaker.dtoes.models.RacetrackDTO;
import tm.salam.TmBookmaker.models.Racetrack;

@Component
public class RacetrackDTOSerializer {

    private final CityDTOSerializer cityDTOSerializer;
    private final RegionDTOSerializer regionDTOSerializer;

    public RacetrackDTOSerializer(CityDTOSerializer cityDTOSerializer, RegionDTOSerializer regionDTOSerializer) {
        this.cityDTOSerializer = cityDTOSerializer;
        this.regionDTOSerializer = regionDTOSerializer;
    }

    public RacetrackDTO toRacetrackDTOGeneral(final Racetrack racetrack){

        if(racetrack==null){
            return null;
        }

        return RacetrackDTO.builder()
                .uuid(racetrack.getUuid())
                .name(racetrack.getName())
                .cityDTO(cityDTOSerializer.toCityDTOOnlyName(racetrack.getCity()))
                .regionDTO(regionDTOSerializer.toRegionDTOOnlyName(racetrack.getRegion()))
                .isActive(racetrack.isActive())
                .build();
    }

    public RacetrackDTO toRacetrackDTOOnlyUuidAndName(final Racetrack racetrack){

        if(racetrack==null){
            return null;
        }

        return RacetrackDTO.builder()
                .uuid(racetrack.getUuid())
                .name(racetrack.getName())
                .build();
    }

    public RacetrackDTO toRacetrackDTO(final Racetrack racetrack){

        if(racetrack==null){
            return null;
        }

        return RacetrackDTO.builder()
                .uuid(racetrack.getUuid())
                .name(racetrack.getName())
                .cityDTO(cityDTOSerializer.toCityDTOOnlyUuidAndName(racetrack.getCity()))
                .regionDTO(regionDTOSerializer.toRegionDTOOnlyUuidAndName(racetrack.getRegion()))
                .isActive(racetrack.isActive())
                .build();
    }

}
