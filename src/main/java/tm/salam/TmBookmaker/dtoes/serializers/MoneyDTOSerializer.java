package tm.salam.TmBookmaker.dtoes.serializers;

import org.springframework.stereotype.Component;
import tm.salam.TmBookmaker.dtoes.models.MoneyDTO;
import tm.salam.TmBookmaker.models.Money;

@Component
public class MoneyDTOSerializer {

    public MoneyDTO toMoneyDTO(final Money money){

        if(money==null){

            return null;
        }

        return MoneyDTO.builder()
                .uuid(money.getUuid())
                .amount(money.getAmount())
                .build();
    }
}
