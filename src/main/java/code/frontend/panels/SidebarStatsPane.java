/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code.frontend.panels;

import code.backend.Countdown.Urgency;
import code.backend.StorageHandler;
import code.frontend.foundation.CustomBox;
import code.frontend.foundation.CustomLine;
import code.frontend.foundation.CustomLine.Type;
import code.frontend.misc.Vals.Colour;
import code.frontend.misc.Vals.FontTools;
import code.frontend.misc.Vals.GraphicalUI;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class SidebarStatsPane extends VBox {
    private static SidebarStatsPane instance = null;

    private static final int LEFTRIGHT_SPACE = 15;
    private static final int TOPBOTTOM_SPACE = 10;

    public static SidebarStatsPane getInstance() {
        if (instance == null)
            instance = new SidebarStatsPane();
        return instance;
    }

    private final StatPane OVERDUE_STAT;
    private final StatPane TODAY_STAT;
    private final StatPane TOMORROW_STAT;

    private SidebarStatsPane() {
        final CustomBox BORDER = new CustomBox(2, 0.011, 0.01, 0);
        CustomBox.applyToPane(this, BORDER);
        BORDER.setStrokeColour(Colour.GHOST);

        this.OVERDUE_STAT = new StatPane(Urgency.OVERDUE);
        this.TODAY_STAT = new StatPane(Urgency.TODAY);
        this.TOMORROW_STAT = new StatPane(Urgency.TOMORROW);
        this.setAlignment(Pos.CENTER);
        this.setBackground(Colour.createBG(Color.BLACK, 13, 8));
        this.getChildren().addAll(this.OVERDUE_STAT, this.TODAY_STAT, this.TOMORROW_STAT);
        VBox.setMargin(
            this.OVERDUE_STAT, new Insets(TOPBOTTOM_SPACE, LEFTRIGHT_SPACE, 0, LEFTRIGHT_SPACE));
        VBox.setMargin(this.TODAY_STAT, new Insets(0, LEFTRIGHT_SPACE, 0, LEFTRIGHT_SPACE));
        VBox.setMargin(
            this.TOMORROW_STAT, new Insets(0, LEFTRIGHT_SPACE, TOPBOTTOM_SPACE, LEFTRIGHT_SPACE));
    }

    public void refreshContent() {
        OVERDUE_STAT.refreshContent();
        TODAY_STAT.refreshContent();
        TOMORROW_STAT.refreshContent();
    }

    private class StatPane extends HBox {
        private final String STRING_LABEL;
        private final Color COLOUR;
        private final Urgency URGENCY;

        private final Label NAME_LABEL;
        private final CustomLine LINE;
        private final Label COUNTER_LABEL;

        private int count;

        StatPane(Urgency urgency) {
            this.count = StorageHandler.getStatistic(urgency);
            this.URGENCY = urgency;
            switch (URGENCY) {
                case OVERDUE:
                    this.STRING_LABEL = "Overdue";
                    this.COLOUR = Colour.CD_OVERDUE;
                    break;
                case TODAY:
                    this.STRING_LABEL = "Due today";
                    this.COLOUR = Colour.CD_TODAY;
                    break;
                case TOMORROW:
                    this.STRING_LABEL = "Due tomorrow";
                    this.COLOUR = Colour.CD_TOMORROW;
                    break;
                default:
                    this.STRING_LABEL = "Others";
                    this.COLOUR = Color.WHITE;
                    break;
            }
            this.NAME_LABEL = new Label();
            this.LINE = new CustomLine(2, Type.HORIZONTAL_TYPE);
            this.COUNTER_LABEL = new Label();
            this.setBackground(null);
            this.setFillHeight(true);
            this.setMinHeight(25);
            this.setAlignment(Pos.CENTER);

            initNameLabel();
            initCounterLabel();
            this.getChildren().addAll(this.NAME_LABEL, createLine(), this.COUNTER_LABEL);
        }

        private void initLabel(Label label) {
            label.setTextFill(this.COLOUR);
            label.setFont(Font.font(FontTools.FONT_FAM, 14));
            label.setAlignment(Pos.CENTER);
        }

        private void initNameLabel() {
            this.NAME_LABEL.setText(this.STRING_LABEL);
            initLabel(this.NAME_LABEL);
        }

        private Pane createLine() {
            final Pane PANE = new Pane();
            CustomLine.applyToPane(PANE, this.LINE);
            this.LINE.setStrokeColour(this.COLOUR);
            this.LINE.setPadding(6);
            PANE.setBackground(null);
            HBox.setHgrow(PANE, Priority.ALWAYS);
            return PANE;
        }

        private void initCounterLabel() {
            this.COUNTER_LABEL.setText(GraphicalUI.intToString(this.count));
            this.COUNTER_LABEL.setMinWidth(10);
            initLabel(this.COUNTER_LABEL);
        }

        private void setOverallColour(Color colour) {
            this.NAME_LABEL.setTextFill(colour);
            this.LINE.setStrokeColour(colour);
            this.COUNTER_LABEL.setTextFill(colour);
        }

        public void refreshContent() {
            this.count = StorageHandler.getStatistic(this.URGENCY);
            this.COUNTER_LABEL.setText(GraphicalUI.intToString(this.count));
            if (this.count == 0) {
                this.setOverallColour(Colour.TXT_GHOST);
                this.setOpacity(0.5);
            } else {
                this.setOverallColour(this.COLOUR);
                this.setOpacity(1);
            }
        }
    }
}
