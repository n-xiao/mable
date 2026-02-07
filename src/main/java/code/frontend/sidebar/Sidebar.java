package code.frontend.sidebar;

import code.frontend.libs.katlaf.ricing.RiceHandler;
import javafx.scene.layout.VBox;

public class Sidebar extends VBox {
    public Sidebar() {
        // TODO
        this.setBackground(RiceHandler.createBG(RiceHandler.getColour("grey"), 0, 0));
        this.setPrefWidth(30);
        this.setMaxHeight(Double.MAX_VALUE);
    }
}
