/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code.frontend.gui.pages.home;

import code.backend.data.Countdown;
import code.backend.data.Countdown.Urgency;
import code.backend.data.CountdownFolder;
import code.backend.data.CountdownFolder.SpecialType;
import code.backend.utils.CountdownHandler;
import code.backend.utils.FolderHandler;
import code.frontend.foundation.panels.inputs.InputField;
import code.frontend.gui.rightclickmenu.CountdownViewRCM;
import code.frontend.gui.rightclickmenu.RightClickMenuTemplate;
import code.frontend.misc.Vals.Colour;
import code.frontend.windows.EditWindow;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.NavigableSet;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;

/**
 * This is the scrollable Pane that is responsible for displaying {@link CountdownPane}.
 * An instance of this class should be added to a {@link Content} instance.
 */
public class CountdownPaneView extends ScrollPane {
    public enum ButtonMode { NO_SELECT, SINGLE_SELECT, MULTI_SELECT }
    public enum DisplayOrder { ASCENDING, DESCENDING }

    protected final double HGAP_BETWEEN = 24;
    protected final double VGAP_BETWEEN = 2;
    protected final FlowPane FLOW_PANE;
    protected final LinkedHashSet<CountdownPane> COUNTDOWN_PANES;
    protected final ArrayList<Region> PADDINGS_IN_USE;

    protected ButtonMode mode;
    protected DisplayOrder displayOrder;
    protected CountdownPane pivot; // for command or ctrl click

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
        this.FLOW_PANE.setAlignment(Pos.TOP_CENTER);
        this.FLOW_PANE.setHgap(HGAP_BETWEEN);
        this.FLOW_PANE.setVgap(VGAP_BETWEEN);
        this.FLOW_PANE.setPadding(new Insets(0, 0, 30, 0));
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
                InputField.escapeAllInputs();
                cpv.requestFocus();
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
        int numOfCountdowns = CountdownHandler.getDescendingCountdowns().size();
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
        FolderHandler.organiseProtectedFolders();
        NavigableSet<Countdown> countdowns;
        // this if-else is ok for now since there's only two DisplayOrders rn
        if (displayOrder.equals(DisplayOrder.ASCENDING))
            countdowns = CountdownHandler.getAscendingCountdowns();
        else
            countdowns = CountdownHandler.getDescendingCountdowns();

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

        CountdownPaneViewTitle.getInstance().updateTitleText();
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
            if (countdownPane.isSelected()) {
                folder.getContents().add(countdownPane.getCountdown());
            }
        }
        markSelectedAsComplete(false);
    }

    public void removeSelectedFromFolder(CountdownFolder folder) {
        final boolean DRAG_FROM_COMPLETED_FOLDER =
            FolderHandler.getCurrentlySelectedFolder().getType() == SpecialType.ALL_COMPLETE;
        for (CountdownPane countdownPane : COUNTDOWN_PANES) {
            if (countdownPane.isSelected())
                folder.getContents().remove(countdownPane.getCountdown());
            if (countdownPane.isSelected() && DRAG_FROM_COMPLETED_FOLDER)
                countdownPane.getCountdown().setDone(false);
        }
        repopulate(LocalDate.now());
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
            // removes from folder if mark as done... yes, bad design... will improve later
            if (isDone && !FolderHandler.getCurrentlySelectedFolder().isProtectedFolder()) {
                removeSelectedFromFolder(FolderHandler.getCurrentlySelectedFolder());
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
        CountdownHandler.deleteCountdowns(selected);
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

    protected void deselect(CountdownPane pane) {
        pane.setSelected(false);
        pane.applyDeselectStyle(true);
    }

    protected void select(CountdownPane pane) {
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

    protected void updateMode() {
        int selections = getNumOfSelections();
        if (selections == 0)
            this.mode = ButtonMode.NO_SELECT;
        else if (selections == 1)
            this.mode = ButtonMode.SINGLE_SELECT;
        else
            this.mode = ButtonMode.MULTI_SELECT;
    }
}
