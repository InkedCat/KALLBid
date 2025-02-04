package fr.umontpellier.iut.enchven;

import fr.umontpellier.iut.queryhelper.QueryCode;
import fr.umontpellier.iut.queryhelper.QueryType;
import fr.umontpellier.iut.queryhelper.SignedQuery;
import fr.umontpellier.iut.shared.Bid;

public class BiddingQuery extends SignedQuery {

    private final String price;

    protected BiddingQuery(QueryType type, QueryCode code, String action, String signature, String encodedKey, String price) {
        super(type, code, action, signature, encodedKey);
        this.price = price;
    }

    public BiddingQuery(QueryType type, QueryCode code, Bid bid) {
        this(type, code, "bidding", bid.signature(), bid.encodedKey(), bid.price());
    }

    public String getPrice() {
        return price;
    }

    @Override
    public String getSignedMessage() {
        return price;
    }
}
