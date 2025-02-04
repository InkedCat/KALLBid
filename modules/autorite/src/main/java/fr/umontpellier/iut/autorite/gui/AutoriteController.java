package fr.umontpellier.iut.autorite.gui;

import fr.umontpellier.iut.autorite.AppAutorite;
import javafx.application.Platform;

public class AutoriteController {

    private final AppAutorite app;

    public AutoriteController(AppAutorite app) {
        this.app = app;
    }

    public void toggleAuction() {
        if (getAuctionState()) {
            app.stopAuction();
        } else {
            app.startAuction();
        }
    }

    public boolean getAuctionState() {
        return app.getAuctionState();
    }

    public void quitAuction() {
        app.cleanRessources();
        Platform.exit();
        System.exit(0);
    }
}