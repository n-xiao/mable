/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code.frontend.panels;

import code.frontend.foundation.CustomLine;
import code.frontend.foundation.CustomLine.Type;
import code.frontend.misc.Vals.Colour;
import code.frontend.misc.Vals.FontTools;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CountdownPaneViewTitle extends VBox {
    private static CountdownPaneViewTitle instance = null;

    private final Label LABEL;

    private CountdownPaneViewTitle() {
        this.setBackground(null);
        this.setFillWidth(true);
        this.LABEL = new Label();
        configureLabel();
        this.getChildren().addAll(this.LABEL, createHorizontalLine());
    }

    public static CountdownPaneViewTitle getInstance() {
        if (instance == null) {
            instance = new CountdownPaneViewTitle();
        }
        return instance;
    }

    private void configureLabel() {
        LABEL.setTextFill(Color.WHITE);
        LABEL.setFont(Font.font(FontTools.FONT_FAM, FontWeight.BOLD, 15));
        LABEL.setAlignment(Pos.CENTER);
        LABEL.setMinHeight(20);
    }

    private static Pane createHorizontalLine() {
        Pane lineContainer = new Pane();
        lineContainer.setBackground(null);
        CustomLine line = new CustomLine(3, Type.HORIZONTAL_TYPE);
        line.setStrokeColour(Colour.TXT_GHOST_2);
        CustomLine.applyToPane(lineContainer, line);
        lineContainer.setMaxWidth(Double.MAX_VALUE);
        lineContainer.setMinHeight(10);
        lineContainer.setOpacity(0.5);
        VBox.setMargin(lineContainer, new Insets(5, 0, 5, 0));
        return lineContainer;
    }

    public void setTitleText(String text) {
        this.LABEL.setText(text);
    }
}
