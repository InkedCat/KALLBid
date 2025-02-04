package fr.umontpellier.iut.vendeur.gui;

import fr.umontpellier.iut.shared.gui.Components.Text.SubtitleFactory;
import fr.umontpellier.iut.socketwrapper.exceptions.RefresherClosedException;
import fr.umontpellier.iut.socketwrapper.exceptions.RemoteUnavailableException;
import fr.umontpellier.iut.socketwrapper.exceptions.SocketClosedException;
import fr.umontpellier.iut.vendeur.AppVendeur;
import fr.umontpellier.iut.vendeur.auction.AuctionWinner;
import fr.umontpellier.iut.vendeur.auction.BiddingObserver;
import fr.umontpellier.iut.vendeur.bids.BidCouple;
import fr.umontpellier.iut.vendeur.gui.components.WinnerContainer;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.security.SignatureException;
import java.util.List;
import java.util.Optional;

public class VendeurController {

    private AppVendeur app;

    public VendeurController(AppVendeur app) {
        this.app = app;
    }

    public void openBids(BorderPane vendeurView) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try {
                    Optional<AuctionWinner> winnerBid =  app.findWinner();
                    winnerBid.ifPresentOrElse(winner -> {
                        Platform.runLater(() -> {
                            vendeurView.setCenter(new WinnerContainer(winner.getWinnerBid(),
                                    String.valueOf(winner.getSecondPrice())));
                        });
                    }, () -> {
                        Platform.runLater(() -> {
                            vendeurView.setCenter(new SubtitleFactory().createSubtitle("Impossible de déterminer le gagnant, veuillez vérifier que plus d'une personne ait enchéri"));
                        });
                    });
                } catch (SocketClosedException | RemoteUnavailableException | IOException |
                         RefresherClosedException e) {
                    vendeurView.setCenter(new SubtitleFactory().createSubtitle("Impossible de joindre l'autorité de gestion, veuillez réessayer plus tard"));
                }

                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public List<BidCouple> viewBids() {
        return app.consultBids();
    }

    public void subscribeToBids(BiddingObserver observer) {
        app.subscribeToBids(observer);
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