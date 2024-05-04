package tm.salam.TmBookmaker.dtoes.serializers;

import org.springframework.stereotype.Component;
import tm.salam.TmBookmaker.dtoes.models.BankDTO;
import tm.salam.TmBookmaker.models.Bank;

@Component
public class BankDTOSerializer {

    public BankDTO toBankDTO(final Bank bank){

        if (bank==null){
            return null;
        }
        return BankDTO.builder()
                .uuid(bank.getUuid())
                .name(bank.getName())
                .build();
    }

    public BankDTO toBankDTOOnlyName(final Bank bank){

        if (bank==null){
            return null;
        }
        return BankDTO.builder()
                .name(bank.getName())
                .build();
    }

}
