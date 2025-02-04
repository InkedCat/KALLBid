package fr.umontpellier.iut.venaut;

import fr.umontpellier.iut.queryhelper.QueryCode;
import fr.umontpellier.iut.queryhelper.QueryType;
import fr.umontpellier.iut.queryhelper.SignedQuery;
import fr.umontpellier.iut.shared.Bid;

import java.util.List;


public class FindWinnerQuery extends SignedQuery {

    private final List<Bid> bids;
    private final String token;

    protected FindWinnerQuery(QueryType type, QueryCode code, String action, String signature,
                           String encodedKey, List<Bid> bids, String token) {
        super(type, code, action, signature, encodedKey);
        this.bids = bids;
        this.token = token;
    }

    public FindWinnerQuery(String signature, String encodedKey, List<Bid> bids, String token) {
        this(QueryType.SINGLE, QueryCode.SUCCESS, "findWinner", signature, encodedKey, bids, token);
    }

    public List<Bid> getBids() {
        return bids;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String getSignedMessage() {
        return bids.toString();
    }
}
