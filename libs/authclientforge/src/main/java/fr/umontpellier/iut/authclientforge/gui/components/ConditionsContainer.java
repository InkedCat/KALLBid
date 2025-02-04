package fr.umontpellier.iut.authclientforge.gui.components;

import fr.umontpellier.iut.shared.gui.Components.Text.TextFactory;
import fr.umontpellier.iut.shared.gui.Redirection;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ConditionsContainer {
    private Label info;
    private VBox container;
    private CheckBox checkBox;

    public ConditionsContainer(){
        VBox container = new VBox();

        HBox box = new HBox();

        Label cgu = (new TextFactory()).createText("CGU",new Insets(4,0,4,0));
        Label space = (new TextFactory()).createText(", ",new Insets(4,0,4,0));
        Label mentions = (new TextFactory()).createText("Mentions Légales",new Insets(4,0,4,0));

        cgu.setUnderline(true);
        initLink(cgu, "/documents/conditions-generales-d-utilisation.pdf");
        mentions.setUnderline(true);
        initLink(mentions, "/documents/mentions-legales.pdf");

        box.getChildren().addAll(cgu,space,mentions);

        this.checkBox = new CheckBox("En cochant cette case, vous confirmez avoir lu et accepté nos CGU et Mention Légales");
        checkBox.setPadding(new Insets(4,0,4,0));
        checkBox.setWrapText(true);
        this.info = (new TextFactory()).createText("",new Insets(4,0,4,0));
        info.setManaged(false);
        info.setVisible(false);
        info.setWrapText(true);
        container.getChildren().addAll(box,checkBox,info);
        this.container = container;
    }

    public void initLink(Label link, String url){
        link.setOnMouseClicked(e->{
            try {
                new Redirection().openLinkInBrowser(String.valueOf(getClass().getResource(url)));
            } catch (Exception err){
                System.out.println("couch");
            }
        });

        link.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                link.setStyle("-fx-text-fill: #0000ff");
            } else {
                link.setStyle("-fx-text-fill: #000000");
            }
        });
    }

    public VBox getContainer(){
        return container;
    }

    public CheckBox getInput(){
        return checkBox;
    }

    public Label getInfo() {
        return info;
    }
}
