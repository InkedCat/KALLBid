package fr.umontpellier.iut.authclientforge.handler;

import fr.umontpellier.iut.queryhelper.QueryCode;
import fr.umontpellier.iut.queryhelper.QueryHandler;
import fr.umontpellier.iut.authclientforge.profile.AuthProfile;
import fr.umontpellier.iut.shared.AuthResult;

import java.util.Optional;

public class AuthResultQueryHandler implements QueryHandler<AuthResult> {
    private AuthProfile profile;

    public AuthResultQueryHandler(AuthProfile profile) {
        this.profile = profile;
    }

    @Override
    public Optional<String> handle(AuthResult query) {
        if (query.getCode().equals(QueryCode.SUCCESS)) {
            profile.authenticate(query.getMessage());
        } else if(query.getCode().equals(QueryCode.ERROR)) {
            profile.logout();
            profile.setLastError(query.getMessage());
        }

        return Optional.empty();
    }
}
