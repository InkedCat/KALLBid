package fr.umontpellier.iut.authclientforge.profile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AuthProfile {
    private String token;
    private String lastError;
    private boolean isAuthenticated;
    private List<AuthProfileObserver> observers;
    private List<AuthErrorObserver> errorObservers;

    public AuthProfile() {
        this.token = null;
        this.isAuthenticated = false;
        this.observers = Collections.synchronizedList(new ArrayList<>());
        this.errorObservers = Collections.synchronizedList(new ArrayList<>());
    }

    public Optional<String> getToken() {
        return Optional.ofNullable(token);
    }

    public synchronized void authenticate(String token) {
        if(token == null) return;

        this.token = token;
        this.isAuthenticated = true;
        notifyObservers();
    }

    public void addObserver(AuthProfileObserver observer) {
        this.observers.add(observer);
    }

    public void addErrorObserver(AuthErrorObserver observer) {
        this.errorObservers.add(observer);
    }

    private void notifyObservers() {
        observers.forEach(observer -> observer.update(isAuthenticated));
    }

    private void notifyErrorObservers() {
        errorObservers.forEach(observer -> observer.update(lastError));
    }

    public void setLastError(String lastError) {
        this.lastError = lastError;
        notifyErrorObservers();
    }

    public Optional<String> getLastError() {
        String copy = this.lastError;
        this.lastError = null;
        return Optional.ofNullable(copy);
    }

    public synchronized void logout() {
        this.token = null;
        this.isAuthenticated = false;
        notifyObservers();
    }

    public synchronized boolean isAuthenticated() {
        return isAuthenticated;
    }
}
