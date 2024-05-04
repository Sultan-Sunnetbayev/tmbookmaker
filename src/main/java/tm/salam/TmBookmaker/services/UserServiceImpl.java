package tm.salam.TmBookmaker.services;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.daoes.UserRepository;
import tm.salam.TmBookmaker.dtoes.models.UserDTO;
import tm.salam.TmBookmaker.dtoes.serializers.UserDTOSerializer;
import tm.salam.TmBookmaker.helpers.ResponseBody;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.models.User;

import java.util.*;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDTOSerializer userDTOSerializer;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserDTOSerializer userDTOSerializer) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDTOSerializer = userDTOSerializer;
    }

    @Override
    @Transactional
    public ResponseTransfer<UUID> addUser(final User user, final UUID roleUuid){

        final ResponseTransfer<UUID> responseTransfer;
        final UUID savedUserUuid=userRepository.addUser(user.getName(), user.getSurname(), user.getLogin(),
                passwordEncoder.encode(user.getPassword()), roleUuid);

        if(savedUserUuid==null){
            responseTransfer=ResponseTransfer.<UUID>builder()
                    .httpStatus(HttpStatus.CONFLICT)
                    .responseBody(ResponseBody.<UUID>builder()
                            .message("error user don't added")
                            .build())
                    .build();
        }else{
            responseTransfer=ResponseTransfer.<UUID>builder()
                    .httpStatus(HttpStatus.CREATED)
                    .responseBody(ResponseBody.<UUID>builder()
                            .data(savedUserUuid)
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?> editUser(final User user){

        final ResponseTransfer<?> responseTransfer;

        final Boolean isEdited=userRepository.editUser(user.getUuid(), user.getName(), user.getSurname(),
                user.getLogin(), passwordEncoder.encode(user.getPassword()));

        if(Boolean.TRUE.equals(isEdited)){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.ACCEPTED)
                    .responseBody(ResponseBody.builder()
                            .message("accept user successful edited")
                            .build())
                    .build();
        }else{
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.builder()
                            .message("error user don't edited")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<UserDTO>getUserDTOByUuid(final UUID uuid){

        final ResponseTransfer<UserDTO> responseTransfer;
        final User user =userRepository.getUserByUuid(uuid);

        if(user!=null){
            responseTransfer=ResponseTransfer.<UserDTO>builder()
                    .httpStatus(HttpStatus.OK)
                    .responseBody(ResponseBody.<UserDTO>builder()
                            .message("accept user successful returned")
                            .data(userDTOSerializer.toCashierDTO(user))
                            .build())
                    .build();
        }else{
            responseTransfer=ResponseTransfer.<UserDTO>builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .responseBody(ResponseBody.<UserDTO>builder()
                            .message("error user not found")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

}
