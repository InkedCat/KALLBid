package fr.umontpellier.iut.encherisseur.gui;

import fr.umontpellier.iut.shared.gui.Components.Controls.ButtonFactory;
import fr.umontpellier.iut.shared.gui.Components.PageLayout.NavbarFactory;
import fr.umontpellier.iut.shared.gui.Components.Text.SubtitleFactory;
import fr.umontpellier.iut.shared.gui.Components.Text.TextFactory;
import fr.umontpellier.iut.shared.gui.Components.Text.TitleFactory;
import fr.umontpellier.iut.shared.gui.ViewFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.List;

public class EncherisseurViewFactory implements ViewFactory {
    private final EncherisseurController controller;
    private final Button disconnectButton;
    private final Button createBidButton;
    private final Label bidElementLabel;
    private final NumFieldFX bidInput;
    private final Label bidInputInfoLabel;

    private void addListeners() {
        disconnectButton.setOnAction(event -> {
            controller.logOut();
        });

        createBidButton.setOnAction(event -> {
            if (bidInput.isValid()) {
                controller.bid(bidInput.getText(), bidElementLabel);
                bidInputInfoLabel.setVisible(true);
                bidInputInfoLabel.setManaged(true);
                bidInputInfoLabel.setText("Mise créée avec succès!");
            }
        });

        bidInput.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                createBidButton.fire();
            }
        });

    }

    public EncherisseurViewFactory(EncherisseurController controller) {
        this.controller = controller;
        disconnectButton = (new ButtonFactory()).createSecondaryButton("Déconnecter");
        createBidButton = (new ButtonFactory()).createPrimaryButton("Créer une mise");
        bidElementLabel = (new TextFactory()).createText("00 €", new Insets(0, 0, 4, 0));
        bidInput = new NumFieldFX();
        bidInputInfoLabel = new Label();

        addListeners();
    }

    private VBox getLeft() {
        VBox container = new VBox();
        container.setMaxWidth(160);
        container.setPadding(new Insets(20, 20, 20, 20));

        Label miseLabel = (new TitleFactory()).createTitle("Mise");

        bidElementLabel.setPrefWidth(160);
        bidElementLabel.setStyle("-fx-background-color: #E5E5E5;-fx-font-size: 16px;-fx-border-radius: 8px;-fx-background-radius: 8px;-fx-padding: 8px; -fx-font-weight: bold;");
        bidElementLabel.setAlignment(Pos.CENTER_RIGHT);

        container.getChildren().addAll(miseLabel, bidElementLabel);
        container.setAlignment(Pos.TOP_CENTER);

        return container;
    }

    private HBox getCenter() {
        HBox root = new HBox();
        root.setAlignment(Pos.CENTER);

        VBox formContainer = new VBox(10);
        formContainer.setPadding(new Insets(20, 20, 20, 20));
        formContainer.setAlignment(Pos.TOP_CENTER);

        Label welcomeLabel = (new TitleFactory()).createTitle("Bienvenue cher client.");

        formContainer.getChildren().addAll(welcomeLabel, createInteractions());

        root.getChildren().add(formContainer);

        return root;
    }

    private VBox createInteractions() {
        VBox formVBox = new VBox(8);

        Label createBidLabel = (new SubtitleFactory()).createSubtitle("Créer une mise");

        Label amountLabel = (new TextFactory()).createText("Montant de la mise :");

        Label bidInputInfoLabel = (new TextFactory()).createText("");
        bidInputInfoLabel.setStyle("-fx-text-fill: #CF0000; -fx-font-size: 12px; -fx-font-weight: bold;");
        bidInputInfoLabel.setWrapText(true);
        bidInputInfoLabel.setVisible(false);
        bidInputInfoLabel.setManaged(false);
        bidInput.setInfoLabel(bidInputInfoLabel);

        formVBox.getChildren().addAll(createBidLabel, amountLabel, bidInput, bidInputInfoLabel, createBidButton);

        return formVBox;
    }

    private VBox createRightBox() {
        VBox rightVBox = new VBox();
        rightVBox.setPadding(new Insets(20, 40, 40, 20));

        Label accountLabel = (new TitleFactory()).createTitle("Compte");

        HBox rightHBox = new HBox(8);

        StackPane rightStackPane = new StackPane();
        ImageView userImage = new ImageView(new Image("images/profile.png"));
        Circle circle = new Circle(15, Color.web("#E5E5E5"));
        userImage.setFitWidth(30);
        userImage.setPreserveRatio(true);

        rightStackPane.getChildren().addAll(circle, userImage);

        Label usernameLabel = (new TextFactory()).createText("Guest");
        rightHBox.setAlignment(Pos.CENTER_LEFT);
        rightHBox.getChildren().addAll(rightStackPane, usernameLabel);

        rightVBox.getChildren().addAll(accountLabel, rightHBox);

        return rightVBox;
    }

    @Override
    public BorderPane getRootPane() {
        BorderPane rootPane = new BorderPane();

        rootPane.setTop(new NavbarFactory().createNavbar(List.of(disconnectButton)));
        rootPane.setCenter(getCenter());
        rootPane.setLeft(getLeft());
        rootPane.setRight(createRightBox());

        return rootPane;
    }
}
