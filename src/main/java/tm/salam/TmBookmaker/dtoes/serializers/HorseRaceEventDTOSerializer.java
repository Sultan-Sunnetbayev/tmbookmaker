package tm.salam.TmBookmaker.dtoes.serializers;

import org.springframework.stereotype.Component;
import tm.salam.TmBookmaker.dtoes.models.HorseRaceEventDTO;
import tm.salam.TmBookmaker.models.HorseRaceEvent;
import tm.salam.TmBookmaker.models.Racetrack;

@Component
public class HorseRaceEventDTOSerializer {

    private final RacetrackDTOSerializer racetrackDTOSerializer;

    public HorseRaceEventDTOSerializer(RacetrackDTOSerializer racetrackDTOSerializer) {
        this.racetrackDTOSerializer = racetrackDTOSerializer;
    }

    public HorseRaceEventDTO toHorseRaceEventDTOGeneral(final HorseRaceEvent horseRaceEvent){

        if(horseRaceEvent==null){

            return null;
        }

        return HorseRaceEventDTO.builder()
                .uuid(horseRaceEvent.getUuid())
                .name(horseRaceEvent.getName())
                .time(horseRaceEvent.getTime())
                .date(horseRaceEvent.getDate())
                .numberHorseRaces(horseRaceEvent.getNumberHorseRaces())
                .numberBets(horseRaceEvent.getNumberBets())
                .build();
    }

    public HorseRaceEventDTO toHorseRaceEventDTO(final HorseRaceEvent horseRaceEvent){

        if(horseRaceEvent==null){

            return null;
        }

        return HorseRaceEventDTO.builder()
                .uuid(horseRaceEvent.getUuid())
                .name(horseRaceEvent.getName())
                .time(horseRaceEvent.getTime())
                .date(horseRaceEvent.getDate())
                .build();
    }

    public HorseRaceEventDTO toHorseRaceEventDTOForBet(final HorseRaceEvent horseRaceEvent){

        if(horseRaceEvent==null){

            return null;
        }

        return HorseRaceEventDTO.builder()
                .uuid(horseRaceEvent.getUuid())
                .name(horseRaceEvent.getName())
                .time(horseRaceEvent.getTime())
                .date(horseRaceEvent.getDate())
                .isActive(horseRaceEvent.isActive())
                .build();
    }

    public HorseRaceEventDTO toHorseRaceEventDTOForBet(final Racetrack racetrack){

        if(racetrack==null){

            return null;
        }

        return HorseRaceEventDTO.builder()
                .uuid(racetrack.getUuid())
                .name(racetrack.getName())
                .isActive(false)
                .build();
    }

}
