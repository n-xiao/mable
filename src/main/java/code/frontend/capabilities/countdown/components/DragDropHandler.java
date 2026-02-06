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

import code.backend.data.Countdown.Urgency;
import code.frontend.MainContainer;
import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.graphics.MableBorder;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

// TODO: CLEANUP
@Deprecated
public class DragDropHandler extends Region {
    private static final MainContainer MC = MainContainer.getInstance();
    private static DragDropHandler instance = null;

    /*
     * One big note here, the drag listener MUST be attached to the scene,
     * so that nodes can still detect the drag release, enter and exit.
     * Just one other weird quirk about JavaFX. If you attach the drag
     * detection to any other node, I don't think it's possible to
     * trigger the other nodes' listeners.
     */
    public static DragDropHandler init() {
        if (instance == null) {
            instance = new DragDropHandler();

            instance.setMouseTransparent(true);

            MC.getScene().setOnMouseDragOver(
                (event) -> { instance.CONTAINER.relocate(event.getSceneX(), event.getSceneY()); });

            MC.widthProperty().addListener(instance.WIDTH_LISTENER);
            MC.heightProperty().addListener(instance.HEIGHT_LISTENER);

            MC.getChildren().add(instance);
        }

        return instance;
    }

    public static void close() {
        // Sidebar.getInstance().getRemovePane().sleep();
        if (instance == null)
            return;
        MC.getChildren().remove(instance);
        MC.getScene().setOnMouseDragOver(null);
        MC.widthProperty().removeListener(instance.WIDTH_LISTENER);
        MC.heightProperty().removeListener(instance.HEIGHT_LISTENER);
        instance = null;
    }

    private final Pane CONTAINER;

    private final ChangeListener<Number> WIDTH_LISTENER;
    private final ChangeListener<Number> HEIGHT_LISTENER;

    public DragDropHandler() {
        this.WIDTH_LISTENER = new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    instance.resize(MC.getWidth(), MC.getHeight());
                }
        };
        this.HEIGHT_LISTENER = new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    instance.resize(MC.getWidth(), MC.getHeight());
                }
        };

        this.CONTAINER = new StackPane();
        this.CONTAINER.setManaged(false);
        this.CONTAINER.setCacheHint(CacheHint.SPEED);
        this.CONTAINER.setBackground(null);
        this.CONTAINER.resize(400, 400);

        this.setManaged(false);
        // this.setBackground(Colour.createBG(Color.BLUE, 0, 0)); // debug
        this.relocate(0, 0);
        this.resize(MC.getWidth(), MC.getHeight());
        this.setViewOrder(-200);

        populateStack();
        // Sidebar.getInstance().getRemovePane().wake();

        this.getChildren().add(this.CONTAINER);
    }

    private void populateStack() {
        final int WIDTH = 230;
        final int HEIGHT = 90;
        final Urgency[] URGENCIES = OldCountdownTable.getInstance().getSelectedUrgencies();
        // max of 3 "preview" panes to be generated
        for (int i = 0; i < 3 && i < URGENCIES.length; i++) {
            final MableBorder BORDER = new MableBorder(2.4, 0.2, 0.42);
            BORDER.setStrokeColour(RiceHandler.getColour("selected"));
            final Pane PANE = new Pane();
            MableBorder.applyToPane(PANE, BORDER);
            PANE.resize(WIDTH, HEIGHT);
            Urgency urgency = URGENCIES[i];
            Color bgColour = RiceHandler.getColour("background1");
            switch (urgency) {
                case OVERDUE:
                    bgColour = RiceHandler.getColour("cdOverdue2");
                    break;
                case TODAY:
                    bgColour = RiceHandler.getColour("cdToday2");
                    break;
                case TOMORROW:
                    bgColour = RiceHandler.getColour("cdTomorrow2");
                    break;
                default:
                    bgColour = RiceHandler.getColour("background1");
                    break;
            }
            PANE.setManaged(false);
            PANE.setBackground(RiceHandler.createBG(bgColour, 14, 2));
            PANE.relocate(0, 0);
            PANE.getTransforms().add(new Rotate((i + 1) * 5, 0, 0));
            CONTAINER.getChildren().add(PANE);
        }

        final Label LABEL = new Label("copying: " + URGENCIES.length);
        LABEL.setBackground(RiceHandler.createBG(RiceHandler.getColour("labelDragndrop"), 0, 0));
        LABEL.setTextFill(RiceHandler.getColour("txtDragndrop"));
        LABEL.setFont(FontHandler.getNormal());
        LABEL.setAlignment(Pos.CENTER);
        LABEL.relocate(10, 0);
        LABEL.resize(100, 30);
        LABEL.setManaged(false);
        LABEL.setMaxWidth(WIDTH);
        CONTAINER.getChildren().add(LABEL);
    }
}
