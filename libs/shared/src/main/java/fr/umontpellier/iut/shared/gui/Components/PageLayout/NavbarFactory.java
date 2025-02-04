package fr.umontpellier.iut.shared.gui.Components.PageLayout;


import fr.umontpellier.iut.shared.gui.Colors.CustomColor;
import fr.umontpellier.iut.shared.gui.Colors.CustomColorsResolver;
import fr.umontpellier.iut.shared.gui.Components.Brand.LogoFactory;
import fr.umontpellier.iut.shared.gui.Components.Controls.ButtonGroupFactory;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.List;


public class NavbarFactory {
    private HBox createBaseNavbar(List<Button> buttons){
        HBox root = new HBox();
        String blue = CustomColorsResolver.resolveColor(CustomColor.BLUE);
        root.setStyle("-fx-background-color: "+blue+"; -fx-padding: 10px 20px");

        HBox logo = (new LogoFactory()).createLogo();
        HBox buttonGroup = (new ButtonGroupFactory()).createButtonGroup(buttons);

        root.getChildren().addAll(logo, buildSpacing(),buttonGroup);
        return root;
    }

    public Region buildSpacing(){
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    public HBox createNavbar(List<Button> buttons){
        return createBaseNavbar(buttons);
    }

    public HBox createSimpleNavbar(){
        ArrayList<Button> buttons = new ArrayList<>();
        return createBaseNavbar(buttons);
    }
}
