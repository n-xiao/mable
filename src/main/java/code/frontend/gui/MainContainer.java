package code.frontend.gui;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class MainContainer extends HBox {
    private static MainContainer instance = null;

    private Sidebar sidebar;
    private Content content;

    private MainContainer() {
        this.sidebar = Sidebar.getInstance();
        this.content = Content.getInstance();
        this.setBackground(null);
        this.setFillHeight(true);
        this.sidebar.setMinWidth(200);
        HBox.setMargin(this.sidebar, new Insets(0, 5, 0, 0));
        HBox.setHgrow(this.content, Priority.ALWAYS);
        this.getChildren().addAll(this.sidebar, this.content);
    }

    public static MainContainer getInstance() {
        if (instance == null) {
            instance = new MainContainer();
        }
        return instance;
    }
}
