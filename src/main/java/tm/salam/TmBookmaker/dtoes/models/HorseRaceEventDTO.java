package tm.salam.TmBookmaker.dtoes.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HorseRaceEventDTO {

    private UUID uuid;
    private String name;
    @DateTimeFormat(pattern = "HH:mm")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime time;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer numberHorseRaces;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer numberBets;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private RacetrackDTO racetrackDTO;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isActive;

}
