package fr.umontpellier.iut.shared;

import fr.umontpellier.iut.queryhelper.Query;
import fr.umontpellier.iut.queryhelper.QueryCode;
import fr.umontpellier.iut.queryhelper.QueryType;

public class AuthQuery extends Query {

    private String username;
    private String password;
    private int option;

    public AuthQuery(QueryType type, QueryCode code, String username, String password, int option) {
        super(type, code, "auth");
        this.username = username;
        this.password = password;
        this.option = option;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getOption() {
        return option;
    }
}