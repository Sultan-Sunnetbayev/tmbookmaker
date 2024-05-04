package tm.salam.TmBookmaker.dtoes.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HorseRaceDTO {

    private UUID uuid;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate date;
    @DateTimeFormat(pattern = "HH:mm")
    @JsonFormat(pattern = "HH:mm")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalTime time;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer numberHorses;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer numberBets;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isActive;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private FileDTO dataFile;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<HorseDTO>horseDTOS;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<BetOptionDTO>betOptionDTOS;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<BetDTO>betDTOS;

}
