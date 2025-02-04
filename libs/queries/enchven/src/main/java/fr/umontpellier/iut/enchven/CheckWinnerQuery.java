package fr.umontpellier.iut.enchven;

import fr.umontpellier.iut.queryhelper.QueryCode;
import fr.umontpellier.iut.queryhelper.QueryType;
import fr.umontpellier.iut.queryhelper.SignedQuery;

public class CheckWinnerQuery extends SignedQuery {
    private final int price;

    protected CheckWinnerQuery(QueryType type, QueryCode code, String action,
                            int price, String signature, String encodedKey) {
        super(type, code, action, signature, encodedKey);
        this.price = price;
    }

    public CheckWinnerQuery(String signature, String encodedKey, int price) {
        this(QueryType.SINGLE, QueryCode.SUCCESS, "checkWinner", price, signature, encodedKey);
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String getSignedMessage() {
        return price + "";
    }
}
