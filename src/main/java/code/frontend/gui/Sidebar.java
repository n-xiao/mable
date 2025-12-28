package code.frontend.gui;

import code.frontend.misc.Vals.Colour;
import javafx.scene.layout.VBox;

public class Sidebar extends VBox {
    private static Sidebar instance = null;

    private Sidebar() {
        // todo
        this.setBackground(Colour.createBG(Colour.SIDE_BAR, 0, 0));
    }

    public static Sidebar getInstance() {
        if (instance == null)
            instance = new Sidebar();
        return instance;
    }
}
