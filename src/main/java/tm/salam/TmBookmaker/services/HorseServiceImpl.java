package tm.salam.TmBookmaker.services;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.daoes.HorseRepository;
import tm.salam.TmBookmaker.dtoes.models.HorseDTO;
import tm.salam.TmBookmaker.dtoes.serializers.HorseDTOSerializer;
import tm.salam.TmBookmaker.helpers.ResponseBody;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.models.Horse;

import java.util.*;

@Service
public class HorseServiceImpl implements HorseService {

    private final HorseRepository horseRepository;
    private final HorseDTOSerializer horseDTOSerializer;
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public HorseServiceImpl(HorseRepository horseRepository, HorseDTOSerializer horseDTOSerializer, JdbcTemplate jdbcTemplate,
                            NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.horseRepository = horseRepository;
        this.horseDTOSerializer = horseDTOSerializer;
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public ResponseTransfer<?> addHorses(List<Horse> horses, UUID horseRaceUuid){

        final ResponseTransfer<?>responseTransfer;
        for (Horse horse : horses) {
            horseRepository.addHorse(horse.getNumber(), horse.isActive(), horseRaceUuid);
        }
        responseTransfer=ResponseTransfer.builder()
                .httpStatus(HttpStatus.CREATED)
                .responseBody(ResponseBody.builder()
                        .message("accept horses successful created")
                        .build())
                .build();

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?>addHorseRaceResult(final Horse[] horses){

        final ResponseTransfer<?>responseTransfer;
        String sqlQuery;

        for(Horse horse:horses) {
//            horseRepository.addHorseRaceResult(horse.getUuid(), horse.getPlace());
            sqlQuery="DO $$ " +
                    "BEGIN " +
                    "       UPDATE horses horse SET place = :horsePlace WHERE horse.uuid = :horseUuid;" +
                    "END $$;";
            Map<String, Object>sqlParameterSource=new HashMap<>();

            sqlParameterSource.put("horseUuid", horse.getUuid());
            sqlParameterSource.put("horsePlace", horse.getPlace());
            namedParameterJdbcTemplate.update(sqlQuery, sqlParameterSource);
        }
        responseTransfer=ResponseTransfer.builder()
                .httpStatus(HttpStatus.ACCEPTED)
                .responseBody(ResponseBody.builder()
                        .message("accept horse race result successful added")
                        .build())
                .build();

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?>switchActivationHorses(final Horse[] horses){

        final ResponseTransfer<?>responseTransfer;

        for(Horse horse:horses) {
            horseRepository.switchActivationHorse(horse.getUuid(), horse.isActive());
        }
        responseTransfer=ResponseTransfer.builder()
                .httpStatus(HttpStatus.ACCEPTED)
                .responseBody(ResponseBody.builder()
                        .message("accept activation horses switched successful")
                        .build())
                .build();

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<List<HorseDTO>>getHorsesByHorseRaceUuidForCreateBet(final UUID horseRaceUuid){

        final ResponseTransfer<List<HorseDTO>>responseTransfer;
        final List<Horse>horses=horseRepository.getHorsesByHorseRaceUuid(horseRaceUuid);
        final List<HorseDTO>horseDTOS=new LinkedList<>();

        for(Horse horse:horses){
            horseDTOS.add(horseDTOSerializer.toHorseDTOForCreateBet(horse));
        }
        responseTransfer= ResponseTransfer.<List<HorseDTO>>builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(ResponseBody.<List<HorseDTO>>builder()
                        .message("accept horses successful returned")
                        .data(horseDTOS)
                        .build())
                .build();

        return responseTransfer;
    }

}
