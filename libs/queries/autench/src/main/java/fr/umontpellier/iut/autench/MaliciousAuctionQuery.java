package fr.umontpellier.iut.autench;

import fr.umontpellier.iut.queryhelper.Query;
import fr.umontpellier.iut.queryhelper.QueryCode;
import fr.umontpellier.iut.queryhelper.QueryType;

public class MaliciousAuctionQuery extends Query {
    protected MaliciousAuctionQuery(QueryType type, QueryCode code, String action) {
        super(type, code, action);
    }

    public MaliciousAuctionQuery() {
        this(QueryType.SINGLE, QueryCode.SUCCESS, "maliciousAuction");
    }
}
