/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code.frontend.panels;

import code.backend.Countdown;
import code.backend.Countdown.Urgency;
import code.backend.CountdownFolder;
import code.backend.StorageHandler;
import code.frontend.foundation.CustomBox;
import code.frontend.foundation.CustomLine;
import code.frontend.foundation.CustomLine.Type;
import code.frontend.gui.CountdownViewRCM;
import code.frontend.gui.RightClickMenuTemplate;
import code.frontend.misc.Vals;
import code.frontend.misc.Vals.Colour;
import code.frontend.misc.Vals.GraphicalUI;
import code.frontend.panels.dragndrop.DragHandler;
import code.frontend.windows.EditWindow;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.NavigableSet;
import java.util.SequencedSet;
import javafx.animation.Animation.Status;
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
    private CountdownPane pivot; // for command or ctrl click

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
        this.setStyle("-fx-background: transparent;"); // important: removes the stupid background
        this.setHbarPolicy(ScrollBarPolicy.NEVER);
        this.setVbarPolicy(ScrollBarPolicy.NEVER);
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

            cpv.FLOW_PANE.setOnMousePressed(event -> {
                if (event.getButton() == MouseButton.SECONDARY)
                    CountdownViewRCM.spawnInstance(event.getSceneX(), event.getSceneY());
                else {
                    cpv.deselectAll();
                    RightClickMenuTemplate.despawnAll();
                }
                event.consume();
            });
        }
        return cpv;
    }

    /**
     * This allows for left to right listing of CountdownPanes in a FlowPane. By
     * adding invisible Regions that act as "spacers" or "paddings", an incomplete
     * row of a FlowPane will be aligned to the left when all children of the
     * FlowPane are centered. It is attached to a listener for when the window
     * is resized.
     *
     * At the time of writing, I can't seem to figure out how to reliably get
     * the extra number of paddings needed. Hence, one extra padding is added
     * and the height of the paddings are locked to 1 pixel, so it should not
     * affect the scrolling experience of the user. (as in, the invisible
     * paddings will probably not let the user scroll down to nothing)
     */
    private void addPaddingForAlignment() {
        this.PADDINGS_IN_USE.forEach(padding -> FLOW_PANE.getChildren().remove(padding));
        this.PADDINGS_IN_USE.clear();
        if (this.COUNTDOWN_PANES.isEmpty())
            return;
        int cdWidth = (int) (CountdownPane.WIDTH + HGAP_BETWEEN * 0.5);
        int width = (int) this.getBoundsInParent().getWidth();
        int numOfCountdowns = StorageHandler.getDescendingCountdowns().size();
        int columns = (int) (width / cdWidth);
        if (columns == 0)
            return;
        int panesOnLast = (int) (numOfCountdowns % columns);
        int remainder = columns - panesOnLast + 1;
        for (int i = 0; i < remainder; i++) {
            Region padding = new Region();
            padding.setMinSize(CountdownPane.WIDTH, 1);
            padding.setMaxSize(CountdownPane.WIDTH, 1);
            // padding.setVisible(true);
            // padding.setBackground(Colour.createBG(Color.PINK, 0, 0));
            this.FLOW_PANE.getChildren().add(padding);
            this.PADDINGS_IN_USE.add(padding);
        }
    }

    public void repopulate(LocalDate now) {
        StorageHandler.organiseProtectedFolders();
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
        final boolean IMBALANCE_DETECTED = countdownIterator.hasNext() || paneIterator.hasNext();
        // removes each extra/unused CountdownPanes safely
        paneIterator.forEachRemaining(pane -> {
            pane.setCountdown(null);
            FLOW_PANE.getChildren().remove(pane);
        });
        this.COUNTDOWN_PANES.removeIf(pane -> pane.getCountdown() == null);
        // creates new CountdownPanes for extra Countdowns
        countdownIterator.forEachRemaining(countdown -> {
            CountdownPane countdownPane = new CountdownPane(countdown, now);
            FLOW_PANE.getChildren().add(countdownPane);
            this.COUNTDOWN_PANES.add(countdownPane);
            countdownPane.refreshContent();
        });

        if (IMBALANCE_DETECTED)
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

    public void addAllSelectedToFolder(CountdownFolder folder) {
        for (CountdownPane countdownPane : COUNTDOWN_PANES) {
            if (countdownPane.isSelected())
                folder.getContents().add(countdownPane.getCountdown());
        }
    }

    public Urgency[] getSelectedUrgencies() {
        Urgency[] urgencies = new Urgency[getNumOfSelections()];
        int i = 0;
        for (CountdownPane pane : COUNTDOWN_PANES) {
            if (pane.isSelected()) {
                urgencies[i] = pane.getCountdown().getUrgency(LocalDate.now());
                i++;
            }
        }
        return urgencies;
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
            deselect(countdownPane);
        }
        this.pivot = null;
        updateMode();
    }

    private void deselect(CountdownPane pane) {
        pane.setSelected(false);
        pane.applyDeselectStyle(true);
    }

    private void select(CountdownPane pane) {
        pane.setSelected(true);
        pane.applySelectStyle();
    }

    public void selectAll() {
        COUNTDOWN_PANES.forEach(pane -> { select(pane); });
        this.pivot = null;
        updateMode();
    }

    /**
     * Selects all panes between pane1 and pane2, inclusive of pane1 and pane2.
     */
    public void selectAllBetween(CountdownPane pane1, CountdownPane pane2) {
        boolean select = false;
        for (CountdownPane countdownPane : COUNTDOWN_PANES) {
            boolean atBounds = countdownPane.equals(pane1) || countdownPane.equals(pane2);
            if (atBounds)
                select = !select;
            if (select || atBounds) {
                select(countdownPane);
            }
        }
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

        private final CustomLine VERTICAL_DIVIDER;
        private final HBox HOVER_HBOX; // container
        private final HBox CONTENT_HBOX; // container
        private final FadeTransition FADE_TR; // for ui hover animation
        private final CustomBox CUSTOM_BORDER; // for selection ui indication

        private Countdown countdown; // points to the backend object
        private boolean isUrgent;
        private boolean selected; // for selection detection

        public CountdownPane(Countdown cd, LocalDate now) {
            this.HOVER_HBOX = new HBox();
            this.CONTENT_HBOX = new HBox();
            this.VERTICAL_DIVIDER = new CustomLine(2, Type.VERTICAL_TYPE);
            this.CD_DESC_LABEL = new Label();
            this.CD_DAYS_LABEL = new Label();
            this.STATUS_LABEL = new Label();
            this.END_DATE_LABEL = new Label();
            this.NAME_LABEL = new Label();
            this.CUSTOM_BORDER = new CustomBox(GraphicalUI.DRAW_THICKNESS, 0.018, 0.035, 0.29);
            this.FADE_TR = new FadeTransition(Duration.millis(300), HOVER_HBOX);
            this.countdown = cd;
            this.isUrgent = false;
            this.selected = false;
            this.setAlignment(Pos.CENTER);
            initContentHBox(now);
            initHoverHBox();
            initSelectable();
            initDraggable();
            this.getChildren().addAll(this.HOVER_HBOX, this.CONTENT_HBOX);
        }

        private void initHoverHBox() {
            int leftRightPadding = 16;
            double height = HEIGHT - CONTENT_HEIGHT;
            this.HOVER_HBOX.setPrefSize(WIDTH, height);
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
            this.HOVER_HBOX.setFillHeight(true);
            this.HOVER_HBOX.getChildren().addAll(this.STATUS_LABEL, spacer, this.END_DATE_LABEL);
            this.HOVER_HBOX.setOpacity(0);

            CONTENT_HBOX.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (selected)
                        return; // do nothing if selected
                    FADE_TR.stop();
                    setMouseEnterAnim(FADE_TR);
                    FADE_TR.playFromStart();
                }
            });

            CONTENT_HBOX.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    final boolean ALREADY_RUNNING =
                        FADE_TR.getStatus() == Status.RUNNING && FADE_TR.getToValue() == 0
                        || HOVER_HBOX.getOpacity() == 0;
                    if (selected || ALREADY_RUNNING)
                        return; // do nothing if selected
                    FADE_TR.stop();
                    setMouseExitAnim(FADE_TR);
                    FADE_TR.playFromStart();
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
            CONTENT_HBOX.setPrefSize(WIDTH, CONTENT_HEIGHT);
            CONTENT_HBOX.setFillHeight(true);
            // adds the border
            CustomBox.applyToPane(CONTENT_HBOX, CUSTOM_BORDER);
            // adds the name display
            CONTENT_HBOX.getChildren().add(createNameLabel(countdown));
            // adds the divider
            CONTENT_HBOX.getChildren().add(createVerticalDivider());
            // adds the day countdown pane
            CONTENT_HBOX.getChildren().add(createCountdownDisplay(countdown, now));
        }

        private Label createNameLabel(Countdown cd) {
            String name = cd.getName();
            NAME_LABEL.setText(name);
            Font nameFont = Font.font(Vals.FontTools.FONT_FAM, FontWeight.SEMI_BOLD, 16);
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
            pane.setPrefSize(DIV_WIDTH, HEIGHT);
            VERTICAL_DIVIDER.setStrokeColour(Color.WHITE);
            VERTICAL_DIVIDER.setOpacity(0.4);
            VERTICAL_DIVIDER.setPadding(20);
            CustomLine.applyToPane(pane, VERTICAL_DIVIDER);
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

        private void configureLabelsForUrgency(Countdown countdown, LocalDate now) {
            String statusString = "Ongoing";
            String dateString = "Due: " + countdown.getStringDueDate(now);
            Color colour = Color.WHITE;
            final Urgency URGENCY = countdown.getUrgency(now);
            this.isUrgent = true;
            switch (URGENCY) {
                case OVERDUE:
                    statusString = "Overdue";
                    colour = Colour.CD_OVERDUE;
                    break;
                case TODAY:
                    statusString = "Due today";
                    colour = Colour.CD_TODAY;
                    break;
                case TOMORROW:
                    statusString = "Due tomorrow";
                    colour = Colour.CD_TOMORROW;
                    break;
                case COMPLETED:
                    statusString = "Completed";
                default:
                    this.isUrgent = false;
                    break;
            }
            this.STATUS_LABEL.setText(statusString);
            this.END_DATE_LABEL.setText(dateString);
            this.setPriorityColour(colour);
        }

        private void configureCountdownLabelsText(Countdown countdown, LocalDate now) {
            int daysLeft = Math.abs(countdown.daysUntilDue(now));
            CD_DAYS_LABEL.setText(Vals.GraphicalUI.intToString(daysLeft));
            String textNoun = (daysLeft != 1) ? "DAYS" : "DAY";
            String textAdverb = (countdown.isOverdue(now)) ? "SINCE DUE" : "LEFT";
            CD_DESC_LABEL.setText(textNoun + "\n" + textAdverb);
        }

        private void initSelectable() {
            CONTENT_HBOX.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    final boolean IS_PRIMARY_MOUSE = event.getButton() == MouseButton.PRIMARY;
                    if (IS_PRIMARY_MOUSE && event.isShiftDown()) {
                        onShiftClick();
                    } else if (IS_PRIMARY_MOUSE && event.isMetaDown()) {
                        onMetaClick();
                    } else if (IS_PRIMARY_MOUSE) {
                        onNormalClick();
                    } else {
                        onSecondaryMousePress(event.getSceneX(), event.getSceneY());
                    }
                    event.consume();
                }
            });
        }

        /**
         * For now, drag and drop is specifically COPYING from one folder to another.
         * Moving may be implemented later, but due to time constraints that should
         * be done at a much later date.
         */
        private void initDraggable() {
            CONTENT_HBOX.setOnDragDetected((event) -> {
                RightClickMenuTemplate.despawnAll();
                this.selected = true;
                this.applySelectStyle();
                CountdownPaneView.getInstance().updateMode();

                this.activateDragStyling();

                CONTENT_HBOX.startFullDrag();
                DragHandler.init();
            });

            CONTENT_HBOX.setOnMouseReleased((event) -> {
                CONTENT_HBOX.setMouseTransparent(false);
                this.deactivateDragStyling();
                DragHandler.close();
            });
        }

        private void activateDragStyling() {
            CountdownPaneView.getInstance().COUNTDOWN_PANES.forEach(pane -> {
                if (pane.isSelected())
                    pane.setOpacity(0.3);
            });
        }

        private void deactivateDragStyling() {
            CountdownPaneView.getInstance().COUNTDOWN_PANES.forEach(
                pane -> { pane.setOpacity(1); });
        }

        private void onShiftClick() {
            if (pivot == null) {
                CountdownPaneView.getInstance().selectAllBetween(COUNTDOWN_PANES.getFirst(), this);
                return;
            }

            final boolean PIVOT_SELECTED = pivot.isSelected();
            CountdownPaneView.getInstance().selectAllBetween(pivot, this);

            if (!PIVOT_SELECTED)
                CountdownPaneView.getInstance().deselect(pivot);
        }

        private void onMetaClick() {
            if (this.selected) {
                CountdownPaneView.getInstance().deselect(this);
            } else {
                CountdownPaneView.getInstance().select(this);
            }
            CountdownPaneView.getInstance().pivot = this;
            updateMode();
            RightClickMenuTemplate.despawnAll();
        }

        private void onNormalClick() {
            if (!this.isSelected()) {
                CountdownPaneView.getInstance().deselectAll();
                CountdownPaneView.getInstance().select(this);
                CountdownPaneView.getInstance().pivot = this;
                updateMode();
                RightClickMenuTemplate.despawnAll();
            }
        }

        private void onSecondaryMousePress(double x, double y) {
            if (!this.selected)
                CountdownPaneView.getInstance().deselectAll();
            applySelectStyle();
            this.selected = true;
            updateMode(); // needs to be called before RightClickMenu opens
            CountdownViewRCM.spawnInstance(x, y);
        }

        public void applyDeselectStyle(boolean withFadeOut) {
            if (withFadeOut) {
                FADE_TR.stop();
                FADE_TR.setToValue(0);
                FADE_TR.setFromValue(HOVER_HBOX.getOpacity());
                FADE_TR.playFromStart();
            }
            CUSTOM_BORDER.setStrokeColour(Color.WHITE);
            STATUS_LABEL.setTextFill(Vals.Colour.TXT_GHOST);
            END_DATE_LABEL.setTextFill(Vals.Colour.TXT_GHOST);
        }

        public void applySelectStyle() {
            FADE_TR.stop();
            HOVER_HBOX.setOpacity(1);
            CUSTOM_BORDER.setStrokeColour(Vals.Colour.SELECTED);
            STATUS_LABEL.setTextFill(Vals.Colour.SELECTED);
            END_DATE_LABEL.setTextFill(Vals.Colour.SELECTED);
        }

        public void setCountdown(Countdown countdown) {
            this.countdown = countdown;
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

        protected void refreshContent() {
            LocalDate now = LocalDate.now();
            NAME_LABEL.setText(this.countdown.getName());
            configureCountdownLabelsText(this.countdown, now);
            configureLabelsForUrgency(this.countdown, now);
        }

        private void setPriorityColour(Color colour) {
            if (this.isUrgent)
                CONTENT_HBOX.setBackground(
                    Colour.createBG(Colour.adjustOpacity(colour, 0.2), 14, 6));
            else
                CONTENT_HBOX.setBackground(null);
        }
    }
}
