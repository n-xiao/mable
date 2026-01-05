/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code.frontend.gui;

import code.frontend.gui.pages.HomePage;
import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;

public class Content extends StackPane {
    private static Content instance = null;

    private Content() {
        this.setBackground(null);
        HomePage homepage = HomePage.getInstance();
        StackPane.setMargin(homepage, new Insets(10));
        this.getChildren().addAll(HomePage.getInstance());
    }

    public static Content getInstance() {
        if (instance == null) {
            instance = new Content();
        }
        return instance;
    }
}
