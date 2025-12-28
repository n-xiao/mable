package code.frontend.gui.pages;

import code.frontend.misc.Vals.Colour;
import code.frontend.panels.CountdownPaneControls;
import code.frontend.panels.CountdownPaneView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class HomePage extends VBox {
    private static HomePage instance = null;
    private CountdownPaneControls controls;
    private CountdownPaneView view;

    private HomePage() {
        this.controls = CountdownPaneControls.getInstance();
        this.view = CountdownPaneView.getInstance();
        this.setFillWidth(true);
        VBox.setVgrow(this.view, Priority.ALWAYS);
        this.setBackground(Colour.createBG(Colour.BACKGROUND, 0, 0));
        this.getChildren().addAll(this.controls, this.view);
    }

    public static HomePage getInstance() {
        if (instance == null)
            instance = new HomePage();
        return instance;
    }
}
