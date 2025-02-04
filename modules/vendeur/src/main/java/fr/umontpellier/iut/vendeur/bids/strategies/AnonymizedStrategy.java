package fr.umontpellier.iut.vendeur.bids.strategies;

import fr.umontpellier.iut.cryptowrapper.keys.KeyConverterService;
import fr.umontpellier.iut.cryptowrapper.signature.SignatureService;
import fr.umontpellier.iut.shared.Bid;
import fr.umontpellier.iut.vendeur.bids.BidCouple;

import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AnonymizedStrategy implements BidListStrategy {

    private BidListStrategy strategy;
    private SignatureService signatureService;
    private KeyConverterService signingKeyConverterService;

    public AnonymizedStrategy(BidListStrategy strategy, SignatureService signatureService,
                              KeyConverterService signingKeyConverter) {
        this.strategy = strategy;
        this.signatureService = signatureService;
        this.signingKeyConverterService = signingKeyConverter;
    }

    private Bid anonymiseBid(Bid bid, String encodedKey) throws SignatureException {
        String signature = signatureService.sign(bid.price());

        return new Bid(signature, encodedKey, bid.price());
    }

    @Override
    public List<BidCouple> getBidCouples(List<BidCouple> bids) {
        List<BidCouple> base = strategy.getBidCouples(bids);

        Collections.shuffle(base);

        ArrayList<BidCouple> anonymized = new ArrayList<>();

        String encodedKey = signingKeyConverterService.keyToString(signatureService.getSigningKeys().getPublic());

        for (BidCouple bidCouple : base) {
            Bid anonymisedBid;

            try {
                anonymisedBid = anonymiseBid(bidCouple.getBid(), encodedKey);
            } catch (SignatureException e) {
                continue;
            }

            anonymized.add(new BidCouple(anonymisedBid, bidCouple.getDate()));
        }

        return anonymized;
    }
}
