package fr.umontpellier.iut.autorite.gui;

import fr.umontpellier.iut.shared.gui.ViewFactory;
import fr.umontpellier.iut.shared.gui.Components.PageLayout.ButtonBarFactory;
import fr.umontpellier.iut.shared.gui.Components.PageLayout.NavbarFactory;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.util.List;

public class AutoriteViewFactory implements ViewFactory {
    private final AutoriteController controller;
    private final SimpleBooleanProperty booleanProperty;
    private final Label labelEnchere;
    private final Button startStopBidButton;

    private void addListeners() {
        booleanProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                labelEnchere.setText("Enchère en cours");
                startStopBidButton.setText("Stop Bid");
            } else {
                labelEnchere.setText("Aucune enchère en cours");
                startStopBidButton.setText("Start Bid");
            }
        });

        startStopBidButton.setOnAction(event -> {
            controller.toggleAuction();
            booleanProperty.setValue(controller.getAuctionState());
        });
    }

    public AutoriteViewFactory(AutoriteController controller) {
        this.controller = controller;
        this.booleanProperty = new SimpleBooleanProperty();

        labelEnchere = new Label("Aucune enchère en cours");
        startStopBidButton = new Button("Start Bid");

        addListeners();
    }

    private HBox getTop() {
        return new NavbarFactory().createSimpleNavbar();
    }

    private HBox getCenter() {
        HBox center = new HBox();
        center.setAlignment(Pos.CENTER);

        labelEnchere.setFont(new Font("System Bold", 30.0));

        center.getChildren().add(labelEnchere);
        return center;
    }

    private HBox getBottom() {
        return new ButtonBarFactory().createBaseButtonBar(List.of(startStopBidButton));
    }

    @Override
    public BorderPane getRootPane() {
        BorderPane rootPane = new BorderPane();
        rootPane.setPrefSize(700.0, 500.0);

        rootPane.setTop(getTop());
        rootPane.setCenter(getCenter());
        rootPane.setBottom(getBottom());

        return rootPane;
    }
}