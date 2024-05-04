package tm.salam.TmBookmaker.dtoes.serializers;

import org.springframework.stereotype.Component;
import tm.salam.TmBookmaker.dtoes.models.RegionDTO;
import tm.salam.TmBookmaker.models.Region;

@Component
public class RegionDTOSerializer {

    public RegionDTO toRegionDTOGeneral(final Region region){

        if(region==null){
            return null;
        }
        return RegionDTO.builder()
                .uuid(region.getUuid())
                .name(region.getName())
                .isActive(region.isActive())
                .build();

    }

    public RegionDTO toRegionDTO(final Region region){

        if(region==null){
            return null;
        }
        return RegionDTO.builder()
                .uuid(region.getUuid())
                .name(region.getName())
                .isActive(region.isActive())
                .build();

    }

    public RegionDTO toRegionDTOOnlyUuidAndName(final Region region){

        if(region==null){
            return null;
        }
        return RegionDTO.builder()
                .uuid(region.getUuid())
                .name(region.getName())
                .build();

    }

    public RegionDTO toRegionDTOOnlyName(final Region region){

        if(region==null){
            return null;
        }

        return RegionDTO.builder()
                .name(region.getName())
                .build();
    }

}
