/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code.frontend.gui.dragndrop;

import code.backend.Countdown.Urgency;
import code.frontend.foundation.custom.CustomBox;
import code.frontend.gui.containers.MainContainer;
import code.frontend.gui.containers.Sidebar;
import code.frontend.gui.pages.home.CountdownPaneView;
import code.frontend.misc.Vals.Colour;
import code.frontend.misc.Vals.FontTools;
import code.frontend.misc.Vals.GraphicalUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;

public class DragHandler extends Region {
    private static final MainContainer MC = MainContainer.getInstance();
    private static DragHandler instance = null;

    /*
     * One big note here, the drag listener MUST be attached to the scene,
     * so that nodes can still detect the drag release, enter and exit.
     * Just one other weird quirk about JavaFX. If you attach the drag
     * detection to any other node, I don't think it's possible to
     * trigger the other nodes' listeners.
     */
    public static DragHandler init() {
        if (instance == null) {
            instance = new DragHandler();

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
        Sidebar.getInstance().getRemovePane().sleep();
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

    public DragHandler() {
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
        Sidebar.getInstance().getRemovePane().wake();

        this.getChildren().add(this.CONTAINER);
    }

    private void populateStack() {
        final int WIDTH = 230;
        final int HEIGHT = 90;
        final Urgency[] URGENCIES = CountdownPaneView.getInstance().getSelectedUrgencies();
        // max of 3 "preview" panes to be generated
        for (int i = 0; i < 3 && i < URGENCIES.length; i++) {
            final CustomBox BORDER = new CustomBox(GraphicalUI.DRAW_THICKNESS, 0.018, 0.035, 0.29);
            BORDER.setStrokeColour(Colour.SELECTED);
            final Pane PANE = new Pane();
            CustomBox.applyToPane(PANE, BORDER);
            PANE.resize(WIDTH, HEIGHT);
            Urgency urgency = URGENCIES[i];
            Color bgColour = Colour.BACKGROUND;
            switch (urgency) {
                case OVERDUE:
                    bgColour = Colour.CD_OVERDUE_2;
                    break;
                case TODAY:
                    bgColour = Colour.CD_TODAY_2;
                    break;
                case TOMORROW:
                    bgColour = Colour.CD_TOMORROW_2;
                    break;
                default:
                    bgColour = Colour.BACKGROUND;
                    break;
            }
            PANE.setManaged(false);
            PANE.setBackground(Colour.createBG(bgColour, 14, 5));
            PANE.relocate(0, 0);
            PANE.getTransforms().add(new Rotate((i + 1) * 5, 0, 0));
            CONTAINER.getChildren().add(PANE);
        }

        final Label LABEL = new Label("copying: " + URGENCIES.length);
        LABEL.setBackground(Colour.createBG(Color.BLACK, 0, 0));
        LABEL.setTextFill(Color.WHITE);
        LABEL.setFont(Font.font(FontTools.FONT_FAM, 13));
        LABEL.setAlignment(Pos.CENTER);
        LABEL.relocate(10, 0);
        LABEL.resize(100, 30);
        LABEL.setManaged(false);
        LABEL.setMaxWidth(WIDTH);
        CONTAINER.getChildren().add(LABEL);
    }
}
