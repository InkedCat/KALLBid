package fr.umontpellier.iut.encherisseur.bidUtils;

import fr.umontpellier.iut.cryptowrapper.encryption.EncryptionService;
import fr.umontpellier.iut.cryptowrapper.encryption.exceptions.EncryptException;
import fr.umontpellier.iut.cryptowrapper.keys.KeyConverterService;
import fr.umontpellier.iut.cryptowrapper.signature.SignatureService;
import fr.umontpellier.iut.shared.Bid;

import java.security.PublicKey;
import java.security.SignatureException;

public class BidBuilder {

    private SignatureService signatureService;
    private KeyConverterService signingKeyConverterService;
    private EncryptionService encryptionService;

    public BidBuilder(SignatureService signatureService, KeyConverterService signingKeyConverterService,
                      EncryptionService encryptionService) {
        this.signatureService = signatureService;
        this.signingKeyConverterService = signingKeyConverterService;
        this.encryptionService = encryptionService;
    }

    private String getEncryptedPrice(String price, PublicKey encryptingKey) throws EncryptException {
        return encryptionService.encrypt(price, encryptingKey);
    }

    private String getSignedPrice(String price) throws SignatureException {
        return signatureService.sign(price);
    }

    private String getEncodedKey(){
        return signingKeyConverterService.keyToString(signatureService.getSigningKeys().getPublic());
    }

    public Bid buildBid(String price, PublicKey encryptingKey) throws SignatureException, EncryptException {
        String encryptedPrice = getEncryptedPrice(price, encryptingKey);

        return new Bid(getSignedPrice(encryptedPrice), getEncodedKey(), encryptedPrice);
    }
}
