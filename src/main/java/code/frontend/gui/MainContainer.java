/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code.frontend.gui;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class MainContainer extends HBox {
    private static MainContainer instance = null;

    private Sidebar sidebar;
    private Content content;

    private MainContainer() {
        this.setBackground(null);
        this.setFillHeight(true);
    }

    public static MainContainer getInstance() {
        if (instance == null) {
            instance = new MainContainer();
        }
        return instance;
    }

    /**
     * This is called separately so that children can reference this full instance within their
     * constructor.
     */
    public void init() {
        this.sidebar = Sidebar.getInstance();
        this.content = Content.getInstance();
        this.sidebar.setMinWidth(250);
        HBox.setMargin(this.sidebar, new Insets(0, 5, 0, 0));
        HBox.setHgrow(this.content, Priority.ALWAYS);
        this.getChildren().addAll(this.sidebar, this.content);
    }
}
