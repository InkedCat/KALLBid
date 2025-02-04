package fr.umontpellier.iut.shared;

import fr.umontpellier.iut.queryhelper.Query;
import fr.umontpellier.iut.queryhelper.QueryCode;
import fr.umontpellier.iut.queryhelper.QueryType;

public class AuthResult extends Query {

    String message;

    public AuthResult(QueryType type, QueryCode code, String message) {
        super(type, code, "authResult");
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}