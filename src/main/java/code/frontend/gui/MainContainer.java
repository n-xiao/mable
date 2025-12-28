package code.frontend.gui;

import javafx.scene.control.SplitPane;

public class MainContainer extends SplitPane {
    private static MainContainer instance = null;

    private Sidebar sidebar;
    private Content content;

    private MainContainer() {
        this.sidebar = Sidebar.getInstance();
        this.content = Content.getInstance();
        this.setBackground(null);
        this.getChildren().addAll(this.sidebar, this.content);
    }

    public static MainContainer getInstance() {
        if (instance == null) {
            instance = new MainContainer();
        }
        return instance;
    }
}
