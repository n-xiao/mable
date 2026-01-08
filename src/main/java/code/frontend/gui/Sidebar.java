/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code.frontend.gui;

import code.frontend.foundation.CustomLine;
import code.frontend.foundation.CustomLine.Type;
import code.frontend.misc.Vals.Colour;
import code.frontend.misc.Vals.FontTools;
import code.frontend.panels.SidebarFolderManager;
import code.frontend.panels.SidebarStatsPane;
import code.frontend.panels.dragndrop.RemovePane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class Sidebar extends VBox {
    // using:
    private static final SidebarStatsPane STATS = SidebarStatsPane.getInstance();
    private static final SidebarFolderManager FOLDER_SELECTOR = SidebarFolderManager.getInstance();
    private static Sidebar instance = null;

    private final RemovePane HOVER_REMOVE_PANE;

    private Sidebar() {
        this.HOVER_REMOVE_PANE = RemovePane.applyTo(STATS);
        this.setBackground(Colour.createBG(Colour.SIDE_BAR, 0, 0));
        this.setFillWidth(true);
    }

    public static Sidebar getInstance() {
        if (instance == null) {
            instance = new Sidebar();
            HBox hudHeading = createSectionHeading("H. U. D.");
            HBox folderHeading = createSectionHeading("Folders");
            instance.getChildren().addAll(hudHeading, STATS, folderHeading, FOLDER_SELECTOR);
            VBox.setMargin(hudHeading, new Insets(10, 0, 0, 0));
            VBox.setMargin(STATS, new Insets(0, 10, 10, 10));
            VBox.setMargin(folderHeading, new Insets(20, 0, 0, 0));
            VBox.setMargin(FOLDER_SELECTOR, new Insets(0, 10, 5, 10));
        }
        return instance;
    }

    private static HBox createSectionHeading(String text) {
        final int MIN_HEIGHT = 20;

        HBox divider = new HBox();
        divider.setMinHeight(MIN_HEIGHT);
        divider.maxWidthProperty().bind(instance.widthProperty());
        divider.setBackground(null);
        divider.setFillHeight(true);

        Label label = new Label(text);
        label.setTextFill(Colour.TXT_GHOST_2);
        label.setFont(Font.font(FontTools.FONT_FAM, FontWeight.BOLD, FontPosture.ITALIC, 13.5));
        label.maxHeightProperty().bind(divider.heightProperty());
        label.setAlignment(Pos.CENTER);
        HBox.setMargin(label, new Insets(0, 5, 0, 5));

        Pane leftLine = createHorizontalLine();
        leftLine.setMinHeight(MIN_HEIGHT);
        leftLine.setMaxWidth(16);
        leftLine.setMinWidth(16);

        Pane rightLine = createHorizontalLine();
        rightLine.setMinHeight(MIN_HEIGHT);
        rightLine.maxWidthProperty().bind(divider.widthProperty());
        rightLine.setVisible(false); // one day, a user setting can make this true :D

        divider.getChildren().addAll(leftLine, label, rightLine);

        return divider;
    }

    private static Pane createHorizontalLine() {
        Pane lineContainer = new Pane();
        lineContainer.setBackground(null);
        CustomLine line = new CustomLine(3, Type.HORIZONTAL_TYPE);
        line.setStrokeColour(Colour.TXT_GHOST_2);
        CustomLine.applyToPane(lineContainer, line);
        HBox.setHgrow(lineContainer, Priority.ALWAYS);
        lineContainer.setOpacity(0.5);
        return lineContainer;
    }

    public RemovePane getRemovePane() {
        return HOVER_REMOVE_PANE;
    }
}
