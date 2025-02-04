package fr.umontpellier.iut.autench;

import fr.umontpellier.iut.queryhelper.Query;
import fr.umontpellier.iut.queryhelper.QueryCode;
import fr.umontpellier.iut.queryhelper.QueryType;

public class KeyQuery extends Query {
    private final String token;

    protected KeyQuery(QueryType type, QueryCode code, String action, String token) {
        super(type, code, action);
        this.token = token;
    }

    public KeyQuery(String token) {
        this(QueryType.SINGLE, QueryCode.SUCCESS, "key", token);
    }

    public String getToken() {
        return token;
    }
}
