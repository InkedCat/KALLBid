package fr.umontpellier.iut.vendeur.gui.components;

import fr.umontpellier.iut.vendeur.auction.BiddingObserver;
import fr.umontpellier.iut.vendeur.bids.BidCouple;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.TilePane;

public class BidGrid extends ScrollPane implements BiddingObserver {
    private TilePane tile;
    private ObservableList<BidView> items;
    private IntegerProperty itemsCounter;

    private void initializeConstraints() {
        tile.setHgap(20);
        tile.setVgap(20);

        tile.setPrefTileHeight(100);
        tile.setPrefTileWidth(350);

        tile.setAlignment(Pos.TOP_LEFT);
        tile.setTileAlignment(Pos.TOP_LEFT);

        tile.setPrefColumns(1);
        tile.setPrefRows(1);
    }

    private void initializeStyle() {
        this.setStyle("-fx-background-color:transparent;");
        this.setPadding(new Insets(20,20,20,20));
        this.setFitToWidth(true);
        this.setFitToHeight(true);
        this.setBorder(Border.EMPTY);
    }

    private void addListeners() {
        items.addListener((ListChangeListener<BidView>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (BidView bidView : change.getAddedSubList()) {
                        tile.getChildren().add(bidView);
                    }
                }

                if(change.wasRemoved()){
                    for (BidView bidView : change.getRemoved()) {
                        tile.getChildren().remove(bidView);
                    }
                }
            }
        });
    }

    public BidGrid() {
        tile = new TilePane();
        items = FXCollections.observableArrayList();
        itemsCounter = new SimpleIntegerProperty(0);
        itemsCounter.bind(Bindings.size(items));

        this.setContent(tile);

        initializeConstraints();
        initializeStyle();
        addListeners();
    }

    public IntegerProperty getItemsCounterProperty() {
        return itemsCounter;
    }

    @Override
    public void update(BidCouple oldBid, BidCouple newBid) {
        Platform.runLater(() -> {
            items.removeIf(bidView -> bidView.getBidCouple().getBid().encodedKey().equals(oldBid.getBid().encodedKey()));
            items.add(new BidView(newBid));
        });

    }

    @Override
    public void update(BidCouple newBid) {
        Platform.runLater(() -> items.add(new BidView(newBid)));
    }
}
