package code.frontend.panels.dragndrop;

import code.frontend.misc.Vals.Colour;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class DragHandler extends Region {
    private static DragHandler instance = null;

    public static DragHandler getInstance() {
        if (instance == null) {
            instance = new DragHandler();
        }
        return instance;
    }

    public DragHandler() {
        this.setManaged(false);
        this.setBackground(Colour.createBG(Color.BLUE, 0, 0)); // debug
    }
}
