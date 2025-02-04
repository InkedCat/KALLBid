package fr.umontpellier.iut.shared.gui.Components.Text;

import fr.umontpellier.iut.shared.gui.Colors.Theme;
import fr.umontpellier.iut.shared.gui.Colors.ThemeStyleResolver;

public abstract class ITextFactory {
    private Theme textTheme;

    public ITextFactory(){
        textTheme = Theme.LIGHT;
    }

    public ITextFactory(Theme theme){
        textTheme = theme;
    }

    public String getColor(){
        return ThemeStyleResolver.resolveTextStyle(textTheme);
    }
}
