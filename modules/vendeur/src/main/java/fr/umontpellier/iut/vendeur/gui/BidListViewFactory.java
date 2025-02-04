package fr.umontpellier.iut.vendeur.gui;

import fr.umontpellier.iut.shared.gui.Components.Text.TextFactory;
import fr.umontpellier.iut.shared.gui.Components.Text.TitleFactory;
import fr.umontpellier.iut.shared.gui.ViewFactory;
import fr.umontpellier.iut.vendeur.gui.components.BidGrid;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class BidListViewFactory implements ViewFactory {
    private BidGrid grid;
    private Label bidsNumber;

    private void addListeners() {
        bidsNumber.textProperty().bind(grid.getItemsCounterProperty().asString());
    }

    public BidListViewFactory(BidGrid grid){
        this.grid = grid;
        bidsNumber = new TitleFactory().createTitle("0",new Insets(0,0,2,0));

        addListeners();
    }

    public VBox getHeaderDesc(){
        VBox content = new VBox(8);
        Label title = new TitleFactory().createTitle("Mises courantes.");
        Label text = new TextFactory().createText("Ensemble des mises qui ont été reçues.");

        content.getChildren().addAll(title,text);

        return content;
    }

    public VBox getHeaderInfo(){
        VBox bidsInfo = new VBox(8);
        bidsInfo.setAlignment(Pos.CENTER_RIGHT);

        Label text = new TextFactory().createText("Mises reçues");

        bidsInfo.getChildren().addAll(bidsNumber,text);

        return bidsInfo;
    }

    public BorderPane getHeader(){
        BorderPane header = new BorderPane();
        header.setPadding(new Insets(20,20,20,25));

        header.setLeft(getHeaderDesc());
        header.setRight(getHeaderInfo());

        return header;
    }

    @Override
    public Pane getRootPane() {
        VBox root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);

        VBox.setVgrow(grid, Priority.ALWAYS);

        root.getChildren().addAll(getHeader(), grid);

        return root;
    }
}
