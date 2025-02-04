package fr.umontpellier.iut.vendeur.gui;

import fr.umontpellier.iut.shared.gui.Components.Controls.ButtonFactory;
import fr.umontpellier.iut.shared.gui.Components.PageLayout.ButtonBarFactory;
import fr.umontpellier.iut.shared.gui.Components.PageLayout.NavbarFactory;
import fr.umontpellier.iut.shared.gui.Components.Text.TextFactory;
import fr.umontpellier.iut.shared.gui.Components.Text.TitleFactory;
import fr.umontpellier.iut.shared.gui.ViewFactory;
import fr.umontpellier.iut.vendeur.gui.components.BidGrid;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.List;

public class VendeurViewFactory implements ViewFactory {
    private BorderPane root;
    private VendeurController controller;
    private Button disconnectButton;
    private Button winnerButton;
    private Button bidsButton;
    private BorderPane winnerComponent;
    private Label baseWinnerLabel;
    private BidGrid bidGrid;
    private Pane bidsComponent;

    private void addListeners() {
        disconnectButton.setOnAction(event -> {
            controller.logOut();
        });

        winnerButton.setOnAction(event -> {
            controller.openBids(winnerComponent);
            winnerComponent.setCenter(baseWinnerLabel);
            root.setCenter(winnerComponent);
        });

        bidsButton.setOnAction(event -> {
            root.setCenter(bidsComponent);
        });
    }

    public VendeurViewFactory(VendeurController controller) {
        this.controller = controller;
        root = new BorderPane();
        disconnectButton = new ButtonFactory().createSecondaryButton("Déconnecter");
        winnerButton = new ButtonFactory().createSecondaryButton("Ouvrir l'enchère");
        bidsButton = new ButtonFactory().createSecondaryButton("Consulter les chiffrés");
        winnerComponent = new BorderPane(new TitleFactory().createTitle("Aucun gagnant pour le moment"));
        baseWinnerLabel = new TitleFactory().createTitle("Chargement...");
        bidGrid = new BidGrid();
        controller.subscribeToBids(bidGrid);
        bidsComponent = new BidListViewFactory(bidGrid).getRootPane();

        addListeners();
    }

    private Pane getBaseCenter() {
        VBox centerVBox = new VBox();
        centerVBox.setAlignment(Pos.CENTER);

        Label welcomeLabel = new TitleFactory().createTitle("Bienvenue, cher vendeur !");
        Label actionLabel = new TextFactory().createText("Cliquez sur un des boutons pour effectuer une action.");
        centerVBox.getChildren().addAll(welcomeLabel, actionLabel);

        return centerVBox;
    }

    private Pane getTop() {
        return new NavbarFactory().createNavbar(List.of(disconnectButton));
    }

    private Pane getBottom() {
        return new ButtonBarFactory().createBaseButtonBar(List.of(winnerButton, bidsButton));
    }

    @Override
    public Pane getRootPane() {
        root.setTop(getTop());
        root.setCenter(getBaseCenter());
        root.setBottom(getBottom());

        return root;
    }
}
