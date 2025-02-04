package fr.umontpellier.iut.authclientforge;

import fr.umontpellier.iut.queryhelper.QueryCode;
import fr.umontpellier.iut.queryhelper.QueryType;
import fr.umontpellier.iut.shared.AuthQuery;

public class SignUp extends ClientAuth {
    @Override
    public AuthQuery createAuthQuery(String username, String password) {
        return new AuthQuery(QueryType.SINGLE, QueryCode.SUCCESS, username, password, 1);
    }
}