package tm.salam.TmBookmaker.dtoes.serializers;

import org.springframework.stereotype.Component;
import tm.salam.TmBookmaker.dtoes.models.UserDTO;
import tm.salam.TmBookmaker.models.User;

@Component
public class UserDTOSerializer {

    public UserDTO toGeneralCashierDTO(final User cashier){

        if(cashier==null){

            return null;
        }

        return UserDTO.builder()
                .name(cashier.getName())
                .surname(cashier.getSurname())
                .build();
    }

    public UserDTO toCashierDTO(final User cashier){

        if(cashier==null){

            return null;
        }

        return UserDTO.builder()
                .uuid(cashier.getUuid())
                .name(cashier.getName())
                .surname(cashier.getSurname())
                .login(cashier.getLogin())
                .build();
    }

}
