package fr.umontpellier.iut.authclientforge;

import fr.umontpellier.iut.authclientforge.profile.AuthErrorObserver;
import fr.umontpellier.iut.authclientforge.profile.AuthProfileObserver;

import java.io.IOException;
import java.util.Optional;

public interface AuthController {
    void login(String username, String password) throws IOException;
    void register(String username, String password) throws IOException;
    boolean isAuthenticated();
    Optional<String> getLastErrorMessage();
    void subscribe(AuthProfileObserver authProfileObserver);
    void subscribeError(AuthErrorObserver authErrorObserver);
    void logout();
}
