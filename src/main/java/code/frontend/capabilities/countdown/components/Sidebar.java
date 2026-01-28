/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code.frontend.capabilities.countdown.components;

import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.graphics.CustomLine;
import code.frontend.libs.katlaf.graphics.CustomLine.Type;
import code.frontend.libs.katlaf.inputfields.InputField;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class Sidebar extends VBox {
    private static Sidebar instance = null;

    private final SidebarStats STATS;
    private final SidebarFolders FOLDER_SELECTOR;
    private final CountdownRemover HOVER_REMOVE_PANE;

    private Sidebar() {
        this.STATS = SidebarStats.getInstance();
        this.FOLDER_SELECTOR = SidebarFolders.getInstance();

        this.HOVER_REMOVE_PANE = CountdownRemover.applyTo(STATS);
        this.setBackground(RiceHandler.createBG(RiceHandler.getColour("background2"), 0, 0));
        this.setFillWidth(true);
    }

    public static Sidebar getInstance() {
        if (instance == null) {
            instance = new Sidebar();
            HBox hudHeading = createSectionHeading("H. U. D.");
            HBox folderHeading = createSectionHeading("Folders");
            instance.getChildren().addAll(
                hudHeading, instance.STATS, folderHeading, instance.FOLDER_SELECTOR);
            VBox.setMargin(hudHeading, new Insets(10, 0, 0, 0));
            VBox.setMargin(instance.STATS, new Insets(0, 10, 10, 10));
            VBox.setMargin(folderHeading, new Insets(20, 0, 0, 0));
            VBox.setMargin(instance.FOLDER_SELECTOR, new Insets(0, 10, 5, 10));

            instance.setOnMousePressed((event) -> {
                InputField.escapeAllInputs();
                CountdownTable.getInstance().deselectAll();
                instance.requestFocus();
            });
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
        label.setTextFill(RiceHandler.getColour("txtGhost2"));
        label.setFont(FontHandler.getHeading(4));
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
        line.setStrokeColour(RiceHandler.getColour("txtGhost2"));
        CustomLine.applyToPane(lineContainer, line);
        HBox.setHgrow(lineContainer, Priority.ALWAYS);
        lineContainer.setOpacity(0.5);
        return lineContainer;
    }

    public CountdownRemover getRemovePane() {
        return HOVER_REMOVE_PANE;
    }
}
