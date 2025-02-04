package fr.umontpellier.iut.vendeur.gui.components;

import fr.umontpellier.iut.vendeur.bids.BidCouple;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class BidView extends VBox {

    private BidCouple bidCouple;

    public BidView(BidCouple bidCouple){
        Label labelSignature = new Label("signature : " + bidCouple.getBid().signature());
        Label labelChiffre = new Label("chiffré : " + bidCouple.getBid().price());
        Label labelKey = new Label("clé publique : " + bidCouple.getBid().encodedKey());
        Label labelDate = new Label("date : " + bidCouple.getDate());
        this.bidCouple = bidCouple;

        this.getChildren().addAll(labelSignature,labelChiffre, labelKey, labelDate);
        this.setSpacing(5);
        this.setPadding(new Insets(15));
        this.setStyle("-fx-background-color: #ECECEC;-fx-background-radius: 8px");
    }

    public BidCouple getBidCouple() {
        return bidCouple;
    }
}
