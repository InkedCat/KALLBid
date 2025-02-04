package fr.umontpellier.iut.cryptowrapper.signature.rsa;

import fr.umontpellier.iut.cryptowrapper.keys.rsa.RSAKeyStringConverter;
import org.junit.jupiter.api.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.*;

public class SHA256_RSASignatureHelperTests {

    @Test
    public void signToB64ShouldReturnValidB64Signature() {
        SHA256_RSASignatureHelper sha256_rsaSignatureHelper = new SHA256_RSASignatureHelper();
        RSAKeyStringConverter rsaKeyStringConverter = new RSAKeyStringConverter();

        String message = "Hello world !";
        String expectedSignature = "Z7ZxdFTlWjVUHAi1VXiiROKs26nUtLR17MCMZ7+OSLelPAhwofMl0WljOJsMWdxyfLlWUxqL2KP7eYc9ajdQz2yy9HCrABDEVJKEJAPtByf7TPWSzNdhcNeNCKBE1DDl5X5FKcqY06txi5CyVfGgWQo9MN23q1uDr8V4Ub0yjIjz9RQxHfmazn4E8nRRhF64bgCij3UNMRtVB1qZcwgZgIb8xXqbfgHC1Hh9+IzhyASeU0ZEltT+EnlL6wW0O5Kwtu7KAHKtawvs9vfIyaGENwsWQ5GBE2z0FjIlKInQqwEC493c7JC/I+s6mG4DmQwtqVud3nGDok61RlRnfr9IvQj1F/hDafolLnJyz+OgdqbSfIdxE3dtklufSWKAXIe6s6WfVJBbTvgqU1ZGDgzClhs7UIqlbAzuuRtkkhzQ7SOlfcRXqMdM+3t/UxafpJyQEQDN5eoxeg1bTiSQKHSQDx11Mmp4n/LcnVMn/GDJeHBcquY7/hr9ZhOlk2PI1lYF1JmuHhV82IfNel/2o+9oCF2X57rtp2SUgAlEqHavIj78XIm/LB7h1Za91vDID7kd7kDpOPnfEgCNTBa6xePDjObY14650hdisjY+qMxb5RrThTP3atdc5LjC1vo3K+79wXjrD0cVa/r1Mn9YDtJpnIlmXJo/E8bAOebUfEiwER8=";
        String stringPrivateKey = "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";
        String signature = null;

        try {
            PrivateKey privateKey = rsaKeyStringConverter.B64StringToPrivateKey(stringPrivateKey);
            signature = sha256_rsaSignatureHelper.signToB64(message, privateKey);
        } catch (Exception e) {
            System.err.println(e);
        }

        assertEquals(expectedSignature, signature);
    }

    @Test
    public void checkB64SignatureShouldReturnTrueWhenSignatureValid() {
        SHA256_RSASignatureHelper sha256_rsaSignatureHelper = new SHA256_RSASignatureHelper();
        RSAKeyStringConverter rsaKeyStringConverter = new RSAKeyStringConverter();

        String message = "Hello world !";
        String signature = "Z7ZxdFTlWjVUHAi1VXiiROKs26nUtLR17MCMZ7+OSLelPAhwofMl0WljOJsMWdxyfLlWUxqL2KP7eYc9ajdQz2yy9HCrABDEVJKEJAPtByf7TPWSzNdhcNeNCKBE1DDl5X5FKcqY06txi5CyVfGgWQo9MN23q1uDr8V4Ub0yjIjz9RQxHfmazn4E8nRRhF64bgCij3UNMRtVB1qZcwgZgIb8xXqbfgHC1Hh9+IzhyASeU0ZEltT+EnlL6wW0O5Kwtu7KAHKtawvs9vfIyaGENwsWQ5GBE2z0FjIlKInQqwEC493c7JC/I+s6mG4DmQwtqVud3nGDok61RlRnfr9IvQj1F/hDafolLnJyz+OgdqbSfIdxE3dtklufSWKAXIe6s6WfVJBbTvgqU1ZGDgzClhs7UIqlbAzuuRtkkhzQ7SOlfcRXqMdM+3t/UxafpJyQEQDN5eoxeg1bTiSQKHSQDx11Mmp4n/LcnVMn/GDJeHBcquY7/hr9ZhOlk2PI1lYF1JmuHhV82IfNel/2o+9oCF2X57rtp2SUgAlEqHavIj78XIm/LB7h1Za91vDID7kd7kDpOPnfEgCNTBa6xePDjObY14650hdisjY+qMxb5RrThTP3atdc5LjC1vo3K+79wXjrD0cVa/r1Mn9YDtJpnIlmXJo/E8bAOebUfEiwER8=";
        String stringPublicKey = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAli3h8UbeNCl3o5BlsE53a+u4ksKKjhE3rRk2eStzM+EHQDpLDo7c8N83plXlDYNYQ82loJFU5HZtAn4UL4TQLiJgbwbEKmG/fEbSkHEDv8s6oone6PJ3/b586Zx0pb9AWS1Xj96BMcFDkqP5+k+YLHxT+ylHws+BQNsadrlJWPIgpVE8XwZTPcr0nmjHwmBkySqsIGYKJ+VCoGUp92D+lKgxfrKe1qp9mdVZmmIygNYFfeKq5JLyKTRvtuu4FZ1tgT0f7jsT+2EboGQTV7LdnslluLRkM8C1z5McJd/GsB3wlKyrEb3cjwJ+PFzilsdI06cGZKD1jE2/nrjd2jtF8vcsz/nZ94wx8kLZ3RwEtCt79HrHWIEFGlWNKYGcHV7nfVwWHhDU1bTzERPIYbdI73/PhurlppZdTUMU6RGaKqESCOz/MYgnMLFgkX2/5CRQ+Q/St57LUJkYLP7mxxHN1PnmPHHcrdAVGnvvSXl0RiYECJXf6dFFBQrua0aGQLjIWwo62yxSl6BUhnJQqZUN9W2ePOGc0jqQxY6otXndWedpgx3nt+pfBHGUR7VB/OrTESz/Nsb7TvTwXQYMyKeCnqdPRa5yZ8AgsSFKrARC5CB4vSOG3An5T8EpNlEBUbT8fMFNFbepLJ2ofxGUY4Os+OzVHrJB0f5QSFTDF7e0HH8CAwEAAQ==";

        try {
            PublicKey publicKey = rsaKeyStringConverter.B64StringToPublicKey(stringPublicKey);
            assertTrue(sha256_rsaSignatureHelper.checkB64Signature(message, signature, publicKey));
        } catch (Exception e) {
            System.err.println(e);
        }

    }

    @Test
    public void checkB64SignatureShouldReturnFalseWhenSignatureInvalid() {
        SHA256_RSASignatureHelper sha256_rsaSignatureHelper = new SHA256_RSASignatureHelper();
        RSAKeyStringConverter rsaKeyStringConverter = new RSAKeyStringConverter();

        String message = "Hello world !!!";
        String signature = "Z7ZxdFTlWjVUHAi1VXiiROKs26nUtLR17MCMZ7+OSLelPAhwofMl0WljOJsMWdxyfLlWUxqL2KP7eYc9ajdQz2yy9HCrABDEVJKEJAPtByf7TPWSzNdhcNeNCKBE1DDl5X5FKcqY06txi5CyVfGgWQo9MN23q1uDr8V4Ub0yjIjz9RQxHfmazn4E8nRRhF64bgCij3UNMRtVB1qZcwgZgIb8xXqbfgHC1Hh9+IzhyASeU0ZEltT+EnlL6wW0O5Kwtu7KAHKtawvs9vfIyaGENwsWQ5GBE2z0FjIlKInQqwEC493c7JC/I+s6mG4DmQwtqVud3nGDok61RlRnfr9IvQj1F/hDafolLnJyz+OgdqbSfIdxE3dtklufSWKAXIe6s6WfVJBbTvgqU1ZGDgzClhs7UIqlbAzuuRtkkhzQ7SOlfcRXqMdM+3t/UxafpJyQEQDN5eoxeg1bTiSQKHSQDx11Mmp4n/LcnVMn/GDJeHBcquY7/hr9ZhOlk2PI1lYF1JmuHhV82IfNel/2o+9oCF2X57rtp2SUgAlEqHavIj78XIm/LB7h1Za91vDID7kd7kDpOPnfEgCNTBa6xePDjObY14650hdisjY+qMxb5RrThTP3atdc5LjC1vo3K+79wXjrD0cVa/r1Mn9YDtJpnIlmXJo/E8bAOebUfEiwER8=";
        String stringPublicKey = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAli3h8UbeNCl3o5BlsE53a+u4ksKKjhE3rRk2eStzM+EHQDpLDo7c8N83plXlDYNYQ82loJFU5HZtAn4UL4TQLiJgbwbEKmG/fEbSkHEDv8s6oone6PJ3/b586Zx0pb9AWS1Xj96BMcFDkqP5+k+YLHxT+ylHws+BQNsadrlJWPIgpVE8XwZTPcr0nmjHwmBkySqsIGYKJ+VCoGUp92D+lKgxfrKe1qp9mdVZmmIygNYFfeKq5JLyKTRvtuu4FZ1tgT0f7jsT+2EboGQTV7LdnslluLRkM8C1z5McJd/GsB3wlKyrEb3cjwJ+PFzilsdI06cGZKD1jE2/nrjd2jtF8vcsz/nZ94wx8kLZ3RwEtCt79HrHWIEFGlWNKYGcHV7nfVwWHhDU1bTzERPIYbdI73/PhurlppZdTUMU6RGaKqESCOz/MYgnMLFgkX2/5CRQ+Q/St57LUJkYLP7mxxHN1PnmPHHcrdAVGnvvSXl0RiYECJXf6dFFBQrua0aGQLjIWwo62yxSl6BUhnJQqZUN9W2ePOGc0jqQxY6otXndWedpgx3nt+pfBHGUR7VB/OrTESz/Nsb7TvTwXQYMyKeCnqdPRa5yZ8AgsSFKrARC5CB4vSOG3An5T8EpNlEBUbT8fMFNFbepLJ2ofxGUY4Os+OzVHrJB0f5QSFTDF7e0HH8CAwEAAQ==";

        try {
            PublicKey publicKey = rsaKeyStringConverter.B64StringToPublicKey(stringPublicKey);
            assertFalse(sha256_rsaSignatureHelper.checkB64Signature(message, signature, publicKey));
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}