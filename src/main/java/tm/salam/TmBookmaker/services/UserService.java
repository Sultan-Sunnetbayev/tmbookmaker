package tm.salam.TmBookmaker.services;

import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.dtoes.models.UserDTO;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.models.User;

import java.util.UUID;

public interface UserService {

    @Transactional
    ResponseTransfer<UUID> addUser(User user, UUID roleUuid);

    @Transactional
    ResponseTransfer<?> editUser(User user);

    ResponseTransfer<UserDTO>getUserDTOByUuid(UUID uuid);
}
