package tm.salam.TmBookmaker.dtoes.serializers;

import org.springframework.stereotype.Component;
import tm.salam.TmBookmaker.dtoes.models.HorseDTO;
import tm.salam.TmBookmaker.models.Horse;

import java.util.LinkedList;
import java.util.List;

@Component
public class HorseDTOSerializer {

    public HorseDTO toHorseDTOGeneral(final Horse horse){

        if(horse==null){

            return null;
        }

        return HorseDTO.builder()
                .uuid(horse.getUuid())
                .number(horse.getNumber())
                .place(horse.getPlace())
                .isActive(horse.isActive())
                .build();
    }

    public List<HorseDTO> toHorseDTOSGeneral(final List<Horse> horses){

        if(horses==null){

            return null;
        }
        final List<HorseDTO>horseDTOS=new LinkedList<>();
        for(Horse horse:horses){
            horseDTOS.add(toHorseDTOGeneral(horse));
        }

        return horseDTOS;
    }

    public HorseDTO toHorseDTOForCreateBet(final Horse horse){

        if(horse==null){

            return null;
        }

        return HorseDTO.builder()
                .uuid(horse.getUuid())
                .number(horse.getNumber())
                .isActive(horse.isActive())
                .build();
    }

    public List<HorseDTO> toHorseDTOSForCreateBet(final List<Horse>horses){

        if(horses==null){
            return null;
        }

        return horses.stream().map(this::toHorseDTOForCreateBet).toList();

    }
    public HorseDTO toHorseDTOForResult(final Horse horse){

        if(horse==null){

            return null;
        }

        return HorseDTO.builder()
                .number(horse.getNumber())
                .place(horse.getPlace())
                .isActive(horse.isActive())
                .build();
    }

}
