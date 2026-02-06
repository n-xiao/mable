/*
    Copyright (C) 2026 Nicholas Siow <nxiao.dev@gmail.com>
    DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package code.frontend.capabilities.countdown.components;

import code.backend.data.CountdownFolder.SpecialType;
import code.backend.utils.FolderHandler;
import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.graphics.MableBorder;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class CountdownRemover extends Pane {
    private final MableBorder BORDER;
    private final Label LABEL;
    private final BorderPane CONTAINER;

    private CountdownRemover() {
        BORDER = new MableBorder(2, 0.2, 0.35);
        LABEL = new Label();
        CONTAINER = new BorderPane();
        MableBorder.applyToPane(this, BORDER);
        configureStyling();

        this.setOnMouseDragReleased((event) -> {
            OldCountdownTable.getInstance().removeSelectedFromFolder(
                FolderHandler.getCurrentlySelectedFolder());
            DragDropHandler.close();
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

    public static CountdownRemover applyTo(Pane pane) {
        CountdownRemover removePane = new CountdownRemover();
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
        LABEL.setFont(FontHandler.getHeading(3));
        CONTAINER.setCenter(LABEL);
        CONTAINER.setBackground(null);
        CONTAINER.prefWidthProperty().bind(this.widthProperty());
        CONTAINER.prefHeightProperty().bind(this.heightProperty());
        this.getChildren().add(CONTAINER);
    }
}
