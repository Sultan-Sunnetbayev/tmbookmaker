package tm.salam.TmBookmaker.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tm.salam.TmBookmaker.daoes.RoleRepository;
import tm.salam.TmBookmaker.helpers.ResponseBody;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;

import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public ResponseTransfer<UUID>getRoleUuidByName(final String name){

        final ResponseTransfer<UUID>responseTransfer;
        final UUID roleUuid=roleRepository.getRoleUuidByName(name);

        if (roleUuid == null) {
            responseTransfer=ResponseTransfer.<UUID>builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .responseBody(ResponseBody.<UUID>builder()
                            .message("error role " + name + " not found")
                            .build())
                    .build();
        }else {
            responseTransfer=ResponseTransfer.<UUID>builder()
                    .httpStatus(HttpStatus.OK)
                    .responseBody(ResponseBody.<UUID>builder()
                            .data(roleUuid)
                            .build())
                    .build();
        }

        return responseTransfer;
    }

}
