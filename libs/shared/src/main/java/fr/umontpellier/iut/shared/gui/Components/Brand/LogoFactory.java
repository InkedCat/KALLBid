package fr.umontpellier.iut.shared.gui.Components.Brand;

import fr.umontpellier.iut.shared.gui.Colors.Theme;
import fr.umontpellier.iut.shared.gui.Components.Text.SubtitleFactory;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class LogoFactory {
    public HBox createLogo(){
        HBox leftHBox = new HBox(10);
        leftHBox.setAlignment(Pos.CENTER_LEFT);

        Label brandNameLabel = new SubtitleFactory(Theme.DARK).createSubtitle("Kall BID Industries");

        ImageView imageView = new ImageView(new Image("images/icon.png"));
        imageView.setFitWidth(25);
        imageView.setPreserveRatio(true);

        leftHBox.getChildren().addAll(brandNameLabel, imageView);
        return leftHBox;
    }
}
