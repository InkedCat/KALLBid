package fr.umontpellier.iut.shared.gui.Components.Text;

import fr.umontpellier.iut.shared.gui.Colors.Theme;
import javafx.geometry.Insets;
import javafx.scene.control.Label;

public class SubtitleFactory extends ITextFactory {
    public SubtitleFactory(){
        super();
    }
    public SubtitleFactory(Theme theme){
        super(theme);
    }
    private Label createBaseSubtitle(String text){
        Label subtitle = new Label(text);
        subtitle.setStyle("-fx-font-weight: 900;-fx-font-size: 16px");
        subtitle.setStyle(subtitle.getStyle()+";"+getColor());
        return subtitle;
    }

    public Label createSubtitle(String text){
        Label subtitle = createBaseSubtitle(text);
        subtitle.setStyle(subtitle.getStyle()+";fx-:4px");
        return subtitle;
    }

    public Label createSubtitle(String text, Insets padding){
        Label subtitle = createBaseSubtitle(text);
        subtitle.setPadding(padding);
        return subtitle;
    }
}