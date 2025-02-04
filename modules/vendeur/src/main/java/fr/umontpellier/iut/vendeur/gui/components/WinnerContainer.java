package fr.umontpellier.iut.vendeur.gui.components;

import fr.umontpellier.iut.shared.Bid;
import fr.umontpellier.iut.shared.gui.Components.Text.TextFactory;
import fr.umontpellier.iut.shared.gui.Components.Text.TitleFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class WinnerContainer extends VBox {

    private Bid bid;
    private String secondPrice;
    private TitleFactory titleFactory;
    private TextFactory textFactory;

    private void initializeStyles() {
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(0,20,0,20));
        this.setFillWidth(false);
    }

    public WinnerContainer(Bid bid, String secondPrice){
        this.bid = bid;
        this.secondPrice = secondPrice;
        this.titleFactory = new TitleFactory();
        this.textFactory = new TextFactory();

        Label title = titleFactory.createTitle("Enchérisseur gagnant");

        getChildren().addAll(title, getWinnerContainer());
        initializeStyles();
    }

    private VBox createWinnerInfo(){
        VBox labels = new VBox();

        Label signatureLabel = textFactory.createText("signature : "+ bid.signature());
        Label winneKeyLabel = textFactory.createText("clé publique : "+ bid.encodedKey());
        Label bidLabel = textFactory.createText("prix à payer : "+ secondPrice + " €");

        labels.setAlignment(Pos.CENTER_RIGHT);
        labels.getChildren().addAll(signatureLabel, winneKeyLabel,bidLabel);

        return labels;
    }

    private ImageView createWinnerTrophy(){
        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(50);

        Image image = new Image(String.valueOf(getClass().getResource("/images/trophy-solid.png")));
        imageView.setImage(image);
        return imageView;
    }

    public HBox createWinnerContainer(ImageView imageView, VBox info){
        HBox hBox = new HBox();
        hBox.setSpacing(25);
        hBox.setPadding(new Insets(10,15,10,15));
        hBox.getChildren().addAll(imageView,info);
        hBox.setAlignment(Pos.CENTER);
        hBox.setStyle("-fx-background-color: #ECECEC;-fx-background-radius: 8px");
        hBox.setFillHeight(false);

        return hBox;
    }

    private HBox getWinnerContainer(){
        ImageView imageView = createWinnerTrophy();
        VBox info = createWinnerInfo();

        return createWinnerContainer(imageView,info);
    }
}
