package fr.umontpellier.iut.encherisseur.gui;

import fr.umontpellier.iut.encherisseur.AppEncherisseur;
import fr.umontpellier.iut.encherisseur.bidUtils.LastBid;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;

import java.util.Optional;

public class EncherisseurController {

    private AppEncherisseur app;

    public EncherisseurController(AppEncherisseur app) {
        this.app = app;
    }

    public void bid(String value, Label bidElementLabel) {
        Task<Optional<LastBid>> task = new Task<>() {
            @Override
            protected Optional<LastBid> call() throws Exception {
                return app.bid(value);
            }
        };

        task.setOnSucceeded(event -> {
            Optional<LastBid> lastBid = task.getValue();
            lastBid.ifPresent(bid -> bidElementLabel.setText(bid.getDecryptedPrice() + " €"));
        });

        new Thread(task).start();
    }

    public void quitAuction() {
        app.cleanRessources();
        Platform.exit();
        System.exit(0);
    }

    public void logOut() {
        app.getAuthProfileManager().logout();
    }
}