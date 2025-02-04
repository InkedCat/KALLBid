package fr.umontpellier.iut.shared.gui;

import javafx.stage.Stage;

public class Redirection {
    public String getAbsoluteUrl(String urlFichier){
        return String.valueOf(getClass().getResource(urlFichier));
    }

    public void openLinkInBrowser(String url) throws Exception {
        ApplicationStub applicationStub = new ApplicationStub();
        applicationStub.setUrlLink(url);
        applicationStub.start(new Stage());
    }
}
