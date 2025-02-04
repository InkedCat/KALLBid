package fr.umontpellier.iut.shared.gui.Components.Controls;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.List;

public class ButtonGroupFactory {
    public HBox createButtonGroup(List<Button> buttons){
        HBox btnGroup = new HBox(10);
        btnGroup.getChildren().addAll(buttons);
        return btnGroup;
    }

    public HBox createButtonFillGroup(List<Button> buttons){
        HBox btnGroup = new HBox(10);

        for(Button btn:buttons){
            HBox.setHgrow(btn, Priority.ALWAYS);
            btn.setPrefWidth(1000);
        }
        btnGroup.getChildren().addAll(buttons);
        return btnGroup;
    }
}
