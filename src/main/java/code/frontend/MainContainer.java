/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code.frontend;

import code.frontend.capabilities.countdown.components.HomePage;
import code.frontend.capabilities.countdown.components.Sidebar;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

public class MainContainer extends StackPane {
    private static MainContainer instance = null;

    private HBox countdownContainer;

    private MainContainer() {
        this.setBackground(null);
    }

    private void initCountdown() {
        this.countdownContainer = new HBox();
        this.countdownContainer.setBackground(null);
        this.countdownContainer.setFillHeight(true);
        HomePage homepage = HomePage.getInstance();
        Sidebar sidebar = Sidebar.getInstance();
        sidebar.setMinWidth(250);
        HBox.setMargin(sidebar, new Insets(0, 5, 0, 0));
        HBox.setMargin(homepage, new Insets(10));
        HBox.setHgrow(homepage, Priority.ALWAYS);
        this.countdownContainer.getChildren().addAll(sidebar, homepage);
        this.getChildren().add(this.countdownContainer);
    }

    public void init() {
        this.initCountdown();
    }

    public static MainContainer getInstance() {
        if (instance == null) {
            instance = new MainContainer();
        }
        return instance;
    }
}
