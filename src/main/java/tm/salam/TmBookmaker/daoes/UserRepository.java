package tm.salam.TmBookmaker.daoes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.models.User;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO user(name, surname, login, password, role_uuid) " +
            "VALUES(:name, :surname, :login, :password, :roleUuid) ON CONFLICT DO NOTHING RETURNING CAST(uuid AS VARCHAR)")
    UUID addUser(@Param("name")String name, @Param("surname")String surname, @Param("login")String login,
                 @Param("password")String password, @Param("roleUuid")UUID roleUuid);

    @Transactional
    @Query(nativeQuery = true, value = "UPDATE user SET name = :name, surname = :surname, login = :login, " +
            "password = :password WHERE user.uuid = :uuid RETURNING TRUE")
    Boolean editUser(@Param("uuid")UUID uuid, @Param("name")String name, @Param("surname")String surname,
                     @Param("login")String login, @Param("password")String password);

    @Query("SELECT user FROM User user WHERE user.login = :login")
    User getUserByLogin(@Param("login")String login);

    @Query("SELECT NEW User(user.uuid AS uuid, user.name AS name, user.surname AS surname) FROM User user WHERE user.uuid = :uuid")
    User getUserByUuid(@Param("uuid")UUID uuid);

}
