package fr.umontpellier.iut.shared.gui.Components.PageLayout;

import fr.umontpellier.iut.shared.gui.Colors.CustomColor;
import fr.umontpellier.iut.shared.gui.Colors.CustomColorsResolver;
import fr.umontpellier.iut.shared.gui.Components.Controls.ButtonGroupFactory;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

public class ButtonBarFactory {

    public HBox createBaseButtonBar(List<Button> buttons){
        HBox root = new HBox();
        root.setAlignment(Pos.CENTER);
        String blue = CustomColorsResolver.resolveColor(CustomColor.BLUE);
        root.setStyle("-fx-background-color: "+blue+"; -fx-padding: 20px 20px");

        HBox buttonGroup = (new ButtonGroupFactory()).createButtonGroup(new ArrayList<>(buttons));

        root.getChildren().addAll(buttonGroup);

        return root;
    }

}
