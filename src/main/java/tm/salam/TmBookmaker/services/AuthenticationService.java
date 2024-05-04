package tm.salam.TmBookmaker.services;

import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.models.Bettor;
import tm.salam.TmBookmaker.models.User;

public interface AuthenticationService {

    ResponseTransfer<?> authenticateUser(User user);

    ResponseTransfer<?> authenticateBettor(Bettor bettor);
}
