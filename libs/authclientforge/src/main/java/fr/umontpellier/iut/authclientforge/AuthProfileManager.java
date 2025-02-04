package fr.umontpellier.iut.authclientforge;

import fr.umontpellier.iut.authclientforge.profile.AuthErrorObserver;
import fr.umontpellier.iut.queryhelper.QuerySerializer;
import fr.umontpellier.iut.authclientforge.profile.AuthProfile;
import fr.umontpellier.iut.authclientforge.profile.AuthProfileObserver;
import fr.umontpellier.iut.shared.AuthQuery;
import fr.umontpellier.iut.socketwrapper.ConnectionRefresher;
import fr.umontpellier.iut.socketwrapper.exceptions.RefresherClosedException;
import fr.umontpellier.iut.socketwrapper.exceptions.RemoteUnavailableException;
import fr.umontpellier.iut.socketwrapper.exceptions.SocketClosedException;

import java.io.IOException;
import java.util.Optional;

public class AuthProfileManager implements AuthController {
    private AuthProfile profile;
    private ConnectionRefresher authServer;

    public AuthProfileManager(AuthProfile profile, ConnectionRefresher authServer) {
        this.profile = profile;
        this.authServer = authServer;
    }

    @Override
    public void login(String username, String password) throws IOException {
        AuthQuery query = new LogIn().createAuthQuery(username, password);
        QuerySerializer<AuthQuery> serializer = new QuerySerializer<>();

        try {
            authServer.getConnection().send(serializer.serialize(query));
        } catch (SocketClosedException | RemoteUnavailableException | IOException | RefresherClosedException e) {
            throw new IOException("Impossible de se connecter au serveur d'authentification.", e);
        }
    }

    @Override
    public void register(String username, String password) throws IOException {
        AuthQuery query = new SignUp().createAuthQuery(username, password);
        QuerySerializer<AuthQuery> serializer = new QuerySerializer<>();

        try {
            authServer.getConnection().send(serializer.serialize(query));
        } catch (SocketClosedException | RemoteUnavailableException | IOException | RefresherClosedException e) {
            throw new IOException("Impossible de se connecter au serveur d'authentification.", e);
        }
    }

    @Override
    public boolean isAuthenticated() {
        return profile.isAuthenticated();
    }

    @Override
    public Optional<String> getLastErrorMessage() {
        return profile.getLastError();
    }

    @Override
    public void subscribe(AuthProfileObserver authProfileObserver) {
        profile.addObserver(authProfileObserver);
    }

    @Override
    public void subscribeError(AuthErrorObserver authErrorObserver) {
        profile.addErrorObserver(authErrorObserver);
    }

    public AuthProfile getProfile() {
        return profile;
    }

    @Override
    public void logout() {
        profile.logout();
    }
}
