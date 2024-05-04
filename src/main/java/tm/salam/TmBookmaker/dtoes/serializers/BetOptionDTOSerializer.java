package tm.salam.TmBookmaker.dtoes.serializers;

import org.springframework.stereotype.Component;
import tm.salam.TmBookmaker.dtoes.models.BetOptionDTO;
import tm.salam.TmBookmaker.models.BetOption;

@Component
public class BetOptionDTOSerializer {


    public BetOptionDTO toBetOptionDTOGeneral(final BetOption betOption){

        if(betOption==null){
            return null;
        }

        return BetOptionDTO.builder()
                .uuid(betOption.getUuid())
                .name(betOption.getName())
                .minAmount(betOption.getMinAmount())
                .maxAmount(betOption.getMaxAmount())
                .isActive(betOption.isActive())
                .build();
    }

    public BetOptionDTO toBetOptionDTO(final BetOption betOption){

        if(betOption==null){
            return null;
        }

        return BetOptionDTO.builder()
                .uuid(betOption.getUuid())
                .name(betOption.getName())
                .odds(betOption.getOdds())
                .minAmount(betOption.getMinAmount())
                .maxAmount(betOption.getMaxAmount())
                .build();
    }

    public BetOptionDTO toBetOptionDTOOnlyUuidAndName(final BetOption betOption){

        if(betOption==null){
            return null;
        }

        return BetOptionDTO.builder()
                .uuid(betOption.getUuid())
                .name(betOption.getName())
                .build();
    }

    public BetOptionDTO toBetOptionDTOOnlyName(final BetOption betOption){

        if(betOption==null){
            return null;
        }

        return BetOptionDTO.builder()
                .name(betOption.getName())
                .build();
    }

}
