/*
   Copyright (C) 2026  Nicholas Siow

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU Affero General Public License as
   published by the Free Software Foundation, either version 3 of the
   License, or (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU Affero General Public License for more details.

   You should have received a copy of the GNU Affero General Public License
   along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package code.frontend.panels;

import code.backend.Countdown;
import code.backend.StorageHandler;
import code.frontend.foundation.CustomBox;
import code.frontend.foundation.CustomLine;
import code.frontend.gui.RightClickMenu;
import code.frontend.misc.Vals;
import code.frontend.misc.Vals.Colour;
import code.frontend.misc.Vals.GraphicalUI;
import code.frontend.windows.EditWindow;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.NavigableSet;
import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

/**
 * This is the scrollable Pane that is responsible for displaying {@link CountdownPane}.
 * An instance of this class should be added to a {@link Content} instance.
 */
public class CountdownPaneView extends ScrollPane {
    public enum ButtonMode { NO_SELECT, SINGLE_SELECT, MULTI_SELECT }
    public enum DisplayOrder { ASCENDING, DESCENDING }

    private final double HGAP_BETWEEN = 20;
    private final double VGAP_BETWEEN = -5;
    private final FlowPane FLOW_PANE;
    private final LinkedHashSet<CountdownPane> COUNTDOWN_PANES;
    private final ArrayList<Region> PADDINGS_IN_USE;

    private ButtonMode mode;
    private DisplayOrder displayOrder;

    private static CountdownPaneView cpv = null;

    private CountdownPaneView() {
        this.mode = ButtonMode.NO_SELECT;
        this.PADDINGS_IN_USE = new ArrayList<Region>();
        this.displayOrder = DisplayOrder.ASCENDING;
        this.COUNTDOWN_PANES = new LinkedHashSet<>();
        this.FLOW_PANE = new FlowPane();
        this.FLOW_PANE.prefWrapLengthProperty().bind(this.widthProperty());
        // the -2 below is needed to correct a small offset when at minHeight
        this.FLOW_PANE.minHeightProperty().bind(this.heightProperty().add(-2));
        this.FLOW_PANE.setMaxHeight(Double.MAX_VALUE);
        this.FLOW_PANE.setBackground(Colour.createBG(Colour.BACKGROUND, 0, 0));
        // this.fp.setBackground(Colour.createBG(Color.BLUE, 0, 0));
        this.FLOW_PANE.setAlignment(Pos.TOP_CENTER);
        this.FLOW_PANE.setHgap(HGAP_BETWEEN);
        this.FLOW_PANE.setVgap(VGAP_BETWEEN);
        this.setBackground(null);
        this.setFitToWidth(true);
        this.setHbarPolicy(ScrollBarPolicy.NEVER);
        this.setVbarPolicy(ScrollBarPolicy.NEVER);
        this.FLOW_PANE.setOnMousePressed(event -> { RightClickMenu.close(); });
        this.setContent(this.FLOW_PANE);
    }

    public static CountdownPaneView getInstance() {
        if (cpv == null) {
            cpv = new CountdownPaneView();
            cpv.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                cpv.addPaddingForAlignment();
            }
            });
        }
        return cpv;
    }

    /**
     * This allows for left to right listing of CountdownPanes in a FlowPane. By
     * adding invisible Regions that act as "spacers" or "paddings", an incomplete
     * row of a FlowPane will be aligned to the left when all children of the
     * FlowPane are centered.
     */
    private void addPaddingForAlignment() {
        this.PADDINGS_IN_USE.forEach(padding -> FLOW_PANE.getChildren().remove(padding));
        this.PADDINGS_IN_USE.clear();
        if (this.COUNTDOWN_PANES.isEmpty())
            return;
        double cdWidth = CountdownPane.WIDTH + HGAP_BETWEEN;
        double cdHeight = CountdownPane.HEIGHT;
        double width = this.FLOW_PANE.getPrefWrapLength();
        int numOfCountdowns = StorageHandler.getDescendingCountdowns().size();
        if (width < cdWidth)
            width = cdWidth * numOfCountdowns; // guess
        int columns = (int) Math.round(width / cdWidth);
        int panesOnLast = (int) (numOfCountdowns % columns);
        int remainder = (panesOnLast > 0) ? columns - panesOnLast : 0;
        for (int i = 0; i < remainder; i++) {
            Region padding = new Region();
            padding.setMinSize(CountdownPane.WIDTH, cdHeight);
            padding.setMaxSize(CountdownPane.WIDTH, cdHeight);
            padding.setVisible(false);
            // padding.setBackground(Colour.createBG(Color.PINK, 0, 0));
            this.FLOW_PANE.getChildren().add(padding);
            this.PADDINGS_IN_USE.add(padding);
        }
    }

    public void repopulate(LocalDate now) {
        NavigableSet<Countdown> countdowns;
        // this if-else is ok for now since there's only two DisplayOrders rn
        if (displayOrder.equals(DisplayOrder.ASCENDING))
            countdowns = StorageHandler.getAscendingCountdowns();
        else
            countdowns = StorageHandler.getDescendingCountdowns();

        Iterator<Countdown> countdownIterator = countdowns.iterator();
        Iterator<CountdownPane> paneIterator = this.COUNTDOWN_PANES.iterator();
        // reuses existing CountdownPanes
        while (paneIterator.hasNext() && countdownIterator.hasNext()) {
            CountdownPane pane = paneIterator.next();
            Countdown countdown = countdownIterator.next();
            pane.setCountdownAndRefresh(countdown);
        }
        // removes each extra/unused CountdownPanes safely
        paneIterator.forEachRemaining(pane -> {
            pane.setCountdownAndRefresh(null);
            FLOW_PANE.getChildren().remove(pane);
        });
        this.COUNTDOWN_PANES.removeIf(pane -> pane.getCountdown() == null);
        // creates new CountdownPanes for extra Countdowns
        countdownIterator.forEachRemaining(countdown -> {
            CountdownPane countdownPane = new CountdownPane(countdown, now);
            FLOW_PANE.getChildren().add(countdownPane);
            this.COUNTDOWN_PANES.add(countdownPane);
        });

        addPaddingForAlignment();
        updateMode();
    }

    public int getNumOfSelections() {
        int selections = 0;
        for (CountdownPane countdownPane : COUNTDOWN_PANES) {
            if (countdownPane.isSelected())
                selections++;
        }
        return selections;
    }

    public void markSelectedAsComplete(boolean isDone) {
        COUNTDOWN_PANES.forEach(pane -> {
            if (pane.isSelected()) {
                pane.getCountdown().setDone(isDone);
            }
        });
        deselectAll();
        repopulate(LocalDate.now());
    }

    public void editSelected() {
        // gets first selected
        for (CountdownPane countdownPane : COUNTDOWN_PANES) {
            if (countdownPane.isSelected()) {
                EditWindow.getInstance(countdownPane.getCountdown());
                return;
            }
        }
    }

    public void addSelectedToFolder() {
        // TODO
    }

    public void deleteSelected() {
        ArrayList<Countdown> selected = new ArrayList<Countdown>();
        COUNTDOWN_PANES.forEach(pane -> {
            if (pane.isSelected()) {
                selected.add(pane.getCountdown());
                FLOW_PANE.getChildren().remove(pane);
            }
        });
        StorageHandler.deleteCountdowns(selected);
        COUNTDOWN_PANES.removeIf(pane -> pane.isSelected());
        deselectAll();
        repopulate(LocalDate.now());
    }

    public void deselectAll() {
        for (CountdownPane countdownPane : COUNTDOWN_PANES) {
            countdownPane.setSelected(false);
            countdownPane.applyDeselectStyle();
        }
        updateMode();
    }

    public boolean allSelectedAreCompleted() {
        for (CountdownPane countdownPane : COUNTDOWN_PANES) {
            if (countdownPane.isSelected() && !countdownPane.getCountdown().isDone()) {
                return false;
            }
        }
        return true;
    }

    public DisplayOrder getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(DisplayOrder displayOrder) {
        this.displayOrder = displayOrder;
    }

    public ButtonMode getMode() {
        return mode;
    }

    private void updateMode() {
        int selections = getNumOfSelections();
        if (selections == 0)
            this.mode = ButtonMode.NO_SELECT;
        else if (selections == 1)
            this.mode = ButtonMode.SINGLE_SELECT;
        else
            this.mode = ButtonMode.MULTI_SELECT;
        CountdownPaneControls.getInstance().setMode();
    }

    private class CountdownPane extends VBox {
        public static final double WIDTH = 280;
        public static final double HEIGHT = 140;
        public static final double NAME_WIDTH = 150;
        public static final double DIV_WIDTH = 10;
        public static final double CONTENT_HEIGHT = 100;

        private final Label CD_DESC_LABEL;
        private final Label CD_DAYS_LABEL;
        private final Label STATUS_LABEL; // for displaying the status on mouse hover
        private final Label END_DATE_LABEL; // for displaying the due date on mouse hover
        private final Label NAME_LABEL;

        private HBox hoverHBox; // container
        private HBox contentHBox; // container
        private FadeTransition ft; // for ui hover animation
        private Countdown countdown; // points to the backend object
        private CustomBox border; // for selection ui indication
        private boolean selected; // for selection detection
        // TODO private CountdownPane prev; // for multi-select functionality

        public CountdownPane(Countdown cd, LocalDate now) {
            this.CD_DESC_LABEL = new Label();
            this.CD_DAYS_LABEL = new Label();
            this.STATUS_LABEL = new Label();
            this.END_DATE_LABEL = new Label();
            this.NAME_LABEL = new Label();
            this.countdown = cd;
            this.selected = false;
            this.setAlignment(Pos.CENTER);
            initContentHBox(now);
            initHoverHBox();
            initSelectable(this);
            this.getChildren().addAll(this.hoverHBox, this.contentHBox);
        }

        private void initHoverHBox() {
            int leftRightPadding = 16;
            double height = HEIGHT - CONTENT_HEIGHT;
            this.hoverHBox = new HBox();
            this.hoverHBox.setPrefSize(WIDTH, height);
            Font font =
                Font.font(Vals.FontTools.FONT_FAM, FontWeight.MEDIUM, FontPosture.ITALIC, 14);
            this.STATUS_LABEL.setAlignment(Pos.BOTTOM_LEFT);
            this.STATUS_LABEL.setFont(font);
            this.STATUS_LABEL.setTextFill(Vals.Colour.TXT_GHOST);
            this.STATUS_LABEL.setMaxSize(WIDTH / 2, height);
            HBox.setMargin(this.STATUS_LABEL, new Insets(0, 0, 0, leftRightPadding));
            Pane spacer = new Pane();
            spacer.setMaxSize(WIDTH, height);
            HBox.setHgrow(spacer, Priority.ALWAYS);
            this.END_DATE_LABEL.setAlignment(Pos.BOTTOM_RIGHT);
            this.END_DATE_LABEL.setFont(font);
            this.END_DATE_LABEL.setTextFill(Vals.Colour.TXT_GHOST);
            this.END_DATE_LABEL.setMaxSize(WIDTH / 2, height);
            HBox.setMargin(this.END_DATE_LABEL, new Insets(0, leftRightPadding, 0, 0));
            this.hoverHBox.setFillHeight(true);
            this.hoverHBox.getChildren().addAll(this.STATUS_LABEL, spacer, this.END_DATE_LABEL);

            ft = new FadeTransition(Duration.millis(300), hoverHBox);

            contentHBox.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    ft.stop();
                    if (selected)
                        return; // do nothing if selected
                    setMouseEnterAnim(ft);
                    // TODO: copy this in its own update method later, to be called by a watchdog
                    LocalDate now = LocalDate.now();
                    String status = countdown.getStatusString(now);
                    String end = countdown.getStringDueDate(now);
                    STATUS_LABEL.setText(status);
                    END_DATE_LABEL.setText("Due: " + end);
                    ft.playFromStart();
                }
            });

            contentHBox.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (selected)
                        return; // do nothing if selected
                    ft.stop();
                    setMouseExitAnim(ft);
                    ft.playFromStart();
                }
            });
        }

        private void setMouseEnterAnim(FadeTransition anim) {
            anim.setFromValue(0);
            anim.setToValue(1);
        }

        private void setMouseExitAnim(FadeTransition anim) {
            anim.setFromValue(1);
            anim.setToValue(0);
        }

        private void initContentHBox(LocalDate now) {
            this.contentHBox = new HBox();
            contentHBox.setPrefSize(WIDTH, CONTENT_HEIGHT);
            contentHBox.setFillHeight(true);
            // adds the border
            this.border = new CustomBox(GraphicalUI.DRAW_THICKNESS);
            CustomBox.applyCustomBorder(contentHBox, border);
            // adds the name display
            contentHBox.getChildren().add(createNameLabel(countdown));
            // adds the divider
            contentHBox.getChildren().add(createVerticalDivider());
            // adds the day countdown pane
            contentHBox.getChildren().add(createCountdownDisplay(countdown, now));
        }

        private Label createNameLabel(Countdown cd) {
            String name = cd.getName();
            NAME_LABEL.setText(name);
            Font nameFont = Font.font(Vals.FontTools.FONT_FAM, FontWeight.SEMI_BOLD, 17);
            NAME_LABEL.setAlignment(Pos.CENTER);
            NAME_LABEL.setTextAlignment(TextAlignment.JUSTIFY);
            NAME_LABEL.setTextOverrun(OverrunStyle.ELLIPSIS);
            NAME_LABEL.setFont(nameFont);
            NAME_LABEL.setTextFill(Color.WHITE);
            NAME_LABEL.setPrefWidth(NAME_WIDTH);
            NAME_LABEL.prefHeightProperty().bind(this.heightProperty());

            HBox.setMargin(NAME_LABEL, new Insets(10, -2, 10, 10));
            HBox.setHgrow(NAME_LABEL, Priority.ALWAYS);
            return NAME_LABEL;
        }

        private Pane createVerticalDivider() {
            Pane pane = new Pane();
            Color colour = Color.rgb(255, 255, 255, 0.3);
            CustomLine separator = new CustomLine(2, CustomLine.Type.VERTICAL_TYPE);
            pane.setPrefSize(DIV_WIDTH, HEIGHT);
            separator.setStrokeColour(colour);
            separator.setPadding(20);
            CustomLine.applyCustomBorder(pane, separator);
            return pane;
        }

        private VBox createCountdownDisplay(Countdown cd, LocalDate now) {
            VBox display = new VBox();

            Font numFont =
                Font.font(Vals.FontTools.FONT_FAM, FontWeight.BOLD, FontPosture.ITALIC, 30);
            CD_DAYS_LABEL.setAlignment(Pos.CENTER);
            CD_DAYS_LABEL.setTextAlignment(TextAlignment.CENTER);
            CD_DAYS_LABEL.setFont(numFont);
            CD_DAYS_LABEL.setTextFill(Color.WHITE);
            CD_DAYS_LABEL.prefWidthProperty().bind(display.widthProperty());

            Font textFont =
                Font.font(Vals.FontTools.FONT_FAM, FontWeight.BOLD, FontPosture.ITALIC, 13);
            CD_DESC_LABEL.setAlignment(Pos.CENTER);
            CD_DESC_LABEL.setTextAlignment(TextAlignment.CENTER);
            CD_DESC_LABEL.setFont(textFont);
            CD_DESC_LABEL.setTextFill(Color.WHITE);
            CD_DESC_LABEL.prefWidthProperty().bind(display.widthProperty());

            configureCountdownLabelsText(countdown, now);

            display.getChildren().addAll(CD_DAYS_LABEL, CD_DESC_LABEL);
            HBox.setMargin(display, new Insets(10, 10, 10, 0));
            HBox.setHgrow(display, Priority.ALWAYS);
            return display;
        }

        private void configureCountdownLabelsText(Countdown countdown, LocalDate now) {
            int daysLeft = Math.abs(countdown.daysUntilDue(now));
            CD_DAYS_LABEL.setText(Vals.GraphicalUI.intToString(daysLeft));
            String textNoun = (daysLeft != 1) ? "DAYS" : "DAY";
            String textAdverb = (countdown.isOverdue(now)) ? "AGO" : "LEFT";
            CD_DESC_LABEL.setText(textNoun + "\n" + textAdverb);
        }

        private void initSelectable(CountdownPane thisInstance) {
            contentHBox.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        thisInstance.onPrimaryMousePress();
                    } else {
                        thisInstance.onSecondaryMousePress(event.getSceneX(), event.getSceneY());
                    }
                    event.consume();
                }
            });
        }

        private void onPrimaryMousePress() {
            if (this.selected) {
                applyDeselectStyle();
            } else {
                applySelectStyle();
            }
            this.selected = !this.selected;
            updateMode();
            RightClickMenu.close();
        }

        private void onSecondaryMousePress(double x, double y) {
            if (!this.selected)
                CountdownPaneView.getInstance().deselectAll();
            applySelectStyle();
            this.selected = true;
            updateMode(); // needs to be called before RightClickMenu opens
            RightClickMenu.openAt(x, y);
        }

        public void applyDeselectStyle() {
            ft.stop();
            hoverHBox.setOpacity(0);
            border.setStrokeColour(Color.WHITE);
            STATUS_LABEL.setTextFill(Vals.Colour.TXT_GHOST);
            END_DATE_LABEL.setTextFill(Vals.Colour.TXT_GHOST);
        }

        public void applySelectStyle() {
            ft.stop();
            hoverHBox.setOpacity(1);
            border.setStrokeColour(Vals.Colour.SELECTED);
            STATUS_LABEL.setTextFill(Vals.Colour.SELECTED);
            END_DATE_LABEL.setTextFill(Vals.Colour.SELECTED);
        }

        protected void setCountdownAndRefresh(Countdown countdown) {
            if (countdown == null)
                return;
            this.countdown = countdown;
            refreshContent();
        }

        public Countdown getCountdown() {
            return countdown;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
            CountdownPaneView.getInstance().updateMode();
        }

        /**
         * Instead of destroying CountdownPanes, use this to reuse old
         * panes and forego unnecessary redrawing of graphics.
         */
        protected void refreshContent() {
            LocalDate now = LocalDate.now();
            String status = this.countdown.getStatusString(now);
            String end = this.countdown.getStringDueDate(now);
            STATUS_LABEL.setText(status);
            END_DATE_LABEL.setText("Due: " + end);
            configureCountdownLabelsText(this.countdown, now);
            NAME_LABEL.setText(this.countdown.getName());
        }
    }
}
