package fr.umontpellier.iut.shared.gui.Components.Text;
import fr.umontpellier.iut.shared.gui.Colors.Theme;
import fr.umontpellier.iut.shared.gui.Colors.ThemeStyleResolver;
import javafx.geometry.Insets;
import javafx.scene.control.Label;

public class TitleFactory extends ITextFactory {
    private Theme theme;

    public TitleFactory(){
        theme = Theme.LIGHT;
    }
    public TitleFactory(Theme theme){
        super(theme);
    }

    // Titre avec le style minimum pour les autres fonctions
    private Label createBaseTitle(String text){
        Label title = new Label(text);
        title.setStyle("-fx-font-weight: 900;-fx-font-size: 24px;");

        String colorStyle = ThemeStyleResolver.resolveTextStyle(theme);
        title.setStyle(title.getStyle()+colorStyle);
        return title;
    }

    public Label createTitle(String text){
        Label title = createBaseTitle(text);
        title.setPadding(new Insets(0,0,8,0));
        return title;
    }

    public Label createTitle(String text, Insets padding){
        Label title = createBaseTitle(text);
        title.setPadding(padding);
        return title;
    }
}