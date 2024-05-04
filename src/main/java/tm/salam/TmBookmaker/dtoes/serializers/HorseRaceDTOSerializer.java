package tm.salam.TmBookmaker.dtoes.serializers;

import org.springframework.stereotype.Component;
import tm.salam.TmBookmaker.dtoes.models.HorseRaceDTO;
import tm.salam.TmBookmaker.models.HorseRace;

import java.util.stream.Collectors;

@Component
public class HorseRaceDTOSerializer {

    private final FileDTOSerializer fileDTOSerializer;
    private final HorseDTOSerializer horseDTOSerializer;
    private final BetOptionDTOSerializer betOptionDTOSerializer;

    public HorseRaceDTOSerializer(FileDTOSerializer fileDTOSerializer, HorseDTOSerializer horseDTOSerializer,
                                  BetOptionDTOSerializer betOptionDTOSerializer) {
        this.fileDTOSerializer = fileDTOSerializer;
        this.horseDTOSerializer = horseDTOSerializer;
        this.betOptionDTOSerializer = betOptionDTOSerializer;
    }

    public HorseRaceDTO toHorseRaceDTOGeneral(final HorseRace horseRace){

        if(horseRace==null){
            return null;
        }

        return HorseRaceDTO.builder()
                .uuid(horseRace.getUuid())
                .date(horseRace.getDate())
                .time(horseRace.getTime())
                .numberHorses(horseRace.getNumberHorses())
                .numberBets(horseRace.getNumberBets())
                .isActive(horseRace.isActive())
                .build();
    }

    public HorseRaceDTO toHorseRaceDTO(final HorseRace horseRace){

        if(horseRace==null){
            return null;
        }

        return HorseRaceDTO.builder()
                .uuid(horseRace.getUuid())
                .date(horseRace.getDate())
                .time(horseRace.getTime())
                .dataFile(fileDTOSerializer.toFileDTOGeneral(horseRace.getDataFile()))
                .horseDTOS(horseDTOSerializer.toHorseDTOSGeneral(horseRace.getHorses()))
                .betOptionDTOS(horseRace.getBetOptions()!=null ?
                        horseRace.getBetOptions().stream()
                                .map(betOptionDTOSerializer::toBetOptionDTOOnlyUuidAndName)
                                .collect(Collectors.toList()) : null)
                .build();
    }

    public HorseRaceDTO toHorseRaceDTOForCreateBets(final HorseRace horseRace){

        return HorseRaceDTO.builder()
                .uuid(horseRace.getUuid())
                .time(horseRace.getTime())
                .isActive(horseRace.isActive())
                .build();
    }

    public HorseRaceDTO toHorseRaceDTOForResult(final HorseRace horseRace){

        return HorseRaceDTO.builder()
                .uuid(horseRace.getUuid())
                .time(horseRace.getTime())
                .dataFile(fileDTOSerializer.toFileDTOGeneral(horseRace.getDataFile()))
                .isActive(horseRace.isActive())
                .build();
    }

    public HorseRaceDTO toHorseRaceDTOForCreateBet(final HorseRace horseRace){

        if(horseRace==null){
            return null;
        }

        return HorseRaceDTO.builder()
                .uuid(horseRace.getUuid())
                .time(horseRace.getTime())
                .date(horseRace.getDate())
                .horseDTOS(horseDTOSerializer.toHorseDTOSForCreateBet(horseRace.getHorses()))
                .build();
    }

}
