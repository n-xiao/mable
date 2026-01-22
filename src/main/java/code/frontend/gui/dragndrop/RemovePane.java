/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code.frontend.gui.dragndrop;

import code.backend.data.CountdownFolder.SpecialType;
import code.backend.utils.FolderHandler;
import code.frontend.foundation.custom.MableBorder;
import code.frontend.gui.pages.home.CountdownPaneView;
import code.frontend.gui.ricing.RiceHandler;
import code.frontend.misc.Vals.FontTools;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class RemovePane extends Pane {
    private final MableBorder BORDER;
    private final Label LABEL;
    private final BorderPane CONTAINER;

    private RemovePane() {
        BORDER = new MableBorder(2, 0.2, 0.35);
        LABEL = new Label();
        CONTAINER = new BorderPane();
        MableBorder.applyToPane(this, BORDER);
        configureStyling();

        this.setOnMouseDragReleased((event) -> {
            CountdownPaneView.getInstance().removeSelectedFromFolder(
                FolderHandler.getCurrentlySelectedFolder());
            DragHandler.close();
        });

        this.setOnMouseDragEntered((event) -> {
            BORDER.setStrokeColour(RiceHandler.getColour("hoverDragndrop"));
            LABEL.setTextFill(RiceHandler.getColour("hoverDragndrop"));
        });
        this.setOnMouseDragExited((event) -> {
            BORDER.setStrokeColour(RiceHandler.getColour("bttnRemove"));
            LABEL.setTextFill(RiceHandler.getColour("bttnRemove"));
        });
    }

    public static RemovePane applyTo(Pane pane) {
        RemovePane removePane = new RemovePane();
        pane.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                removePane.resize(pane.getWidth(), pane.getHeight());
            }
        });
        pane.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                removePane.resize(pane.getWidth(), pane.getHeight());
            }
        });
        removePane.setViewOrder(-2);
        removePane.setVisible(false);
        removePane.setManaged(false);
        pane.getChildren().add(removePane);
        removePane.relocate(0, 0);

        return removePane;
    }

    public void wake() {
        if (!(FolderHandler.getCurrentlySelectedFolder().getType() == SpecialType.ALL_INCOMPLETE))
            this.setVisible(true);
    }

    public void sleep() {
        this.setVisible(false);
    }

    private void configureStyling() {
        BORDER.setStrokeColour(RiceHandler.getColour("bttnRemove"));
        final Color BG_COLOUR = RiceHandler.getColour("background2");
        this.setBackground(RiceHandler.createBG(BG_COLOUR, 18, 3));
        LABEL.setTextFill(RiceHandler.getColour("bttnRemove"));
        LABEL.setText("Remove from folder");
        LABEL.setFont(Font.font(FontTools.FONT_FAM, FontWeight.BOLD, FontPosture.ITALIC, 13));
        CONTAINER.setCenter(LABEL);
        CONTAINER.setBackground(null);
        CONTAINER.prefWidthProperty().bind(this.widthProperty());
        CONTAINER.prefHeightProperty().bind(this.heightProperty());
        this.getChildren().add(CONTAINER);
    }
}
