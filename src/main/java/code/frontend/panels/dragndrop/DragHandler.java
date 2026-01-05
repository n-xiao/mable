/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code.frontend.panels.dragndrop;

import code.backend.Countdown.Urgency;
import code.frontend.foundation.CustomBox;
import code.frontend.gui.MainContainer;
import code.frontend.misc.Vals.Colour;
import code.frontend.misc.Vals.FontTools;
import code.frontend.misc.Vals.GraphicalUI;
import code.frontend.panels.CountdownPaneView;
import javafx.scene.CacheHint;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;

public class DragHandler extends Region {
    private static final MainContainer MC = MainContainer.getInstance();
    private static DragHandler instance = null;

    public static DragHandler init() {
        if (instance == null) {
            instance = new DragHandler();

            instance.setMouseTransparent(true);
            MC.getScene().setOnMouseDragOver(
                (event) -> { instance.STACK_PANE.relocate(event.getSceneX(), event.getSceneY()); });

            MC.widthProperty().addListener(
                (event) -> { instance.resize(MC.getWidth(), MC.getHeight()); });
            MC.heightProperty().addListener(
                (event) -> { instance.resize(MC.getWidth(), MC.getHeight()); });

            MC.getChildren().add(instance);
        }

        return instance;
    }

    public static void close() {
        MC.getChildren().remove(instance);
        instance = null;
    }

    private final StackPane STACK_PANE;

    public DragHandler() {
        this.STACK_PANE = new StackPane();
        this.STACK_PANE.setManaged(false);
        this.STACK_PANE.setCacheHint(CacheHint.SPEED);
        this.STACK_PANE.setBackground(null);
        this.STACK_PANE.resize(400, 400);

        this.setManaged(false);
        // this.setBackground(Colour.createBG(Color.BLUE, 0, 0)); // debug
        this.setOpacity(0.2);
        this.relocate(0, 0);
        this.resize(MC.getWidth(), MC.getHeight());
        this.setViewOrder(-200);

        populateStack();

        this.getChildren().add(this.STACK_PANE);
    }

    private void populateStack() {
        final int WIDTH = 240;
        final int HEIGHT = 100;
        final Urgency[] URGENCIES = CountdownPaneView.getInstance().getSelectedUrgencies();
        // max of 3 "preview" panes to be generated
        for (int i = 0; i < 3 && i < URGENCIES.length; i++) {
            final CustomBox BORDER = new CustomBox(GraphicalUI.DRAW_THICKNESS, 0.018, 0.035, 0.29);
            BORDER.setStrokeColour(Colour.SELECTED);
            final Pane PANE = new Pane();
            PANE.setPrefSize(WIDTH, HEIGHT);
            Urgency urgency = URGENCIES[i];
            Color bgColour;
            switch (urgency) {
                case OVERDUE:
                    bgColour = Colour.CD_OVERDUE;
                    break;
                case TODAY:
                    bgColour = Colour.CD_TODAY;
                    break;
                case TOMORROW:
                    bgColour = Colour.CD_TOMORROW;
                    break;
                default:
                    bgColour = Colour.BACKGROUND;
                    break;
            }
            PANE.setBackground(Colour.createBG(Colour.adjustOpacity(bgColour, 0.2), 14, 6));
            PANE.relocate(0, 0);
            PANE.getTransforms().add(new Rotate(i * 10, 0, 0));
            STACK_PANE.getChildren().add(PANE);
        }

        final Label LABEL = new Label("copying: " + URGENCIES.length);
        LABEL.setBackground(Colour.createBG(Color.BLACK, 0, 0));
        LABEL.setTextFill(Color.WHITE);
        LABEL.setFont(Font.font(FontTools.FONT_FAM, FontWeight.BOLD, 12));
        LABEL.relocate(0, 0);
        LABEL.setMinHeight(20);
        LABEL.setMaxWidth(WIDTH);
        STACK_PANE.getChildren().add(LABEL);
    }
}
