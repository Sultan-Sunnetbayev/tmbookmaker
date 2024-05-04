package tm.salam.TmBookmaker.services;

import tm.salam.TmBookmaker.helpers.ResponseTransfer;

import java.util.UUID;

public interface RoleService {
    ResponseTransfer<UUID> getRoleUuidByName(String name);
}
