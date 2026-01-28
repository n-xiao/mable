/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code.frontend.capabilities.countdown.components;

import code.backend.utils.FolderHandler;
import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.graphics.CustomLine;
import code.frontend.libs.katlaf.graphics.CustomLine.Type;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class CountdownFolderTitle extends VBox {
    private static CountdownFolderTitle instance = null;

    private final Label LABEL;

    private CountdownFolderTitle() {
        this.setBackground(null);
        this.setFillWidth(true);
        this.LABEL = new Label();
        configureLabel();
        this.getChildren().addAll(this.LABEL, createHorizontalLine());
    }

    public static CountdownFolderTitle getInstance() {
        if (instance == null) {
            instance = new CountdownFolderTitle();
        }
        return instance;
    }

    private void configureLabel() {
        LABEL.setTextFill(RiceHandler.getColour());
        LABEL.setFont(FontHandler.getHeading(1));
        LABEL.setAlignment(Pos.CENTER);
    }

    private static Pane createHorizontalLine() {
        Pane lineContainer = new Pane();
        lineContainer.setBackground(null);
        CustomLine line = new CustomLine(3, Type.HORIZONTAL_TYPE);
        line.setStrokeColour(RiceHandler.getColour("txtGhost2"));
        CustomLine.applyToPane(lineContainer, line);
        lineContainer.setMaxWidth(Double.MAX_VALUE);
        lineContainer.setMinHeight(5);
        lineContainer.setOpacity(0.5);
        VBox.setMargin(lineContainer, new Insets(5, 0, 10, 0));
        return lineContainer;
    }

    public void updateTitleText() {
        this.LABEL.setText(FolderHandler.getCurrentlySelectedFolder().getName());
    }
}
