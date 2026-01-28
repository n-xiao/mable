/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code.frontend.capabilities.countdown.components;

import code.frontend.libs.katlaf.ricing.RiceHandler;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class CountdownContainer extends VBox {
    private static CountdownContainer instance = null;
    private CountdownFolderTitle title;
    private CountdownTable view;

    private CountdownContainer() {
        this.title = CountdownFolderTitle.getInstance();
        this.view = CountdownTable.getInstance();
        this.setFillWidth(true);
        VBox.setVgrow(this.view, Priority.ALWAYS);
        this.setBackground(RiceHandler.createBG(RiceHandler.getColour("background1"), 0, 0));
        this.getChildren().addAll(this.title, this.view);
    }

    public static CountdownContainer getInstance() {
        if (instance == null)
            instance = new CountdownContainer();
        return instance;
    }
}
