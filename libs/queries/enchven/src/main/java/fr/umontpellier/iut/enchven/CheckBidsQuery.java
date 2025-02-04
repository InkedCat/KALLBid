package fr.umontpellier.iut.enchven;

import fr.umontpellier.iut.queryhelper.QueryCode;
import fr.umontpellier.iut.queryhelper.QueryType;
import fr.umontpellier.iut.queryhelper.SignedQuery;
import fr.umontpellier.iut.shared.Bid;

import java.util.List;

public class CheckBidsQuery extends SignedQuery {
    private final List<Bid> bids;

    protected CheckBidsQuery(QueryType type, QueryCode code, String action, List<Bid> bids, String signature, String encodedKey) {
        super(type, code, action, signature, encodedKey);
        this.bids = bids;
    }

    public CheckBidsQuery(String signature, String encodedKey, List<Bid> bids) {
        this(QueryType.SINGLE, QueryCode.SUCCESS, "checkBids", bids, signature, encodedKey);
    }

    public List<Bid> getBids() {
        return bids;
    }

    @Override
    public String getSignedMessage() {
        return bids.toString();
    }
}
