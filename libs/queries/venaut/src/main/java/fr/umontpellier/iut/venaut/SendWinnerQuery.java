package fr.umontpellier.iut.venaut;

import fr.umontpellier.iut.queryhelper.QueryCode;
import fr.umontpellier.iut.queryhelper.QueryType;
import fr.umontpellier.iut.queryhelper.SignedQuery;
import fr.umontpellier.iut.shared.Winner;

public class SendWinnerQuery extends SignedQuery {
    private final String encryptedPrice;
    private final int secondPrice;

    protected SendWinnerQuery(QueryType type, QueryCode code, String action, String signature,
                           String encodedKey, String encryptedPrice, int secondPrice) {
        super(type, code, action, signature, encodedKey);
        this.encryptedPrice = encryptedPrice;
        this.secondPrice = secondPrice;
    }

    public SendWinnerQuery(String signature,
                           String encodedKey,  String encryptedPrice, int secondPrice) {
        this(QueryType.SINGLE, QueryCode.SUCCESS, "sendWinner", signature, encodedKey, encryptedPrice, secondPrice);
    }

    public String getEncryptedPrice() {
        return encryptedPrice;
    }

    public int getSecondPrice() {
        return secondPrice;
    }

    @Override
    public String getSignedMessage() {
        return new Winner(encryptedPrice, secondPrice).getMessage();
    }
}
