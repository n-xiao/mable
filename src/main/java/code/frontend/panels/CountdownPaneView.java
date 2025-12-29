package code.frontend.panels;

import code.backend.Countdown;
import code.backend.StorageHandler;
import code.frontend.gui.MainContainer;
import code.frontend.misc.Vals.Colour;
import code.frontend.panels.CountdownPaneControls.ControlMode;
import java.awt.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.NavigableSet;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

/**
 * This is the scrollable Pane that is responsible for displaying {@link CountdownPane}.
 * An instance of this class should be added to a {@link Content} instance.
 */
public class CountdownPaneView extends ScrollPane {
    enum DisplayOrder { ASCENDING, DESCENDING }
    private final double HGAP_BETWEEN = 20;
    private final double VGAP_BETWEEN = -5;
    private FlowPane fp;
    private LinkedHashSet<CountdownPane> cdPanes;
    private DisplayOrder displayOrder;
    private ArrayList<Region> paddingsInUse;

    private static CountdownPaneView cpv = null;

    private CountdownPaneView() {
        this.paddingsInUse = new ArrayList<Region>();
        this.displayOrder = DisplayOrder.ASCENDING;
        this.cdPanes = new LinkedHashSet<>();
        this.fp = new FlowPane();
        this.fp.prefWrapLengthProperty().bind(this.widthProperty());
        // the -2 below is needed to correct a small offset when at minHeight
        this.fp.minHeightProperty().bind(this.heightProperty().add(-2));
        this.fp.setMaxHeight(Double.MAX_VALUE);
        this.fp.setBackground(Colour.createBG(Colour.BACKGROUND, 0, 0));
        // this.fp.setBackground(Colour.createBG(Color.BLUE, 0, 0));
        this.fp.setAlignment(Pos.TOP_CENTER);
        this.fp.setHgap(HGAP_BETWEEN);
        this.fp.setVgap(VGAP_BETWEEN);
        this.setBackground(null);
        this.setFitToWidth(true);
        this.setHbarPolicy(ScrollBarPolicy.NEVER);
        this.setVbarPolicy(ScrollBarPolicy.NEVER);
        this.setContent(this.fp);
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

    public void repopulate(LocalDate now) {
        Countdown[] countdowns;
        // this if-else is ok for now since there's only two DisplayOrders rn
        if (displayOrder.equals(DisplayOrder.ASCENDING))
            countdowns = StorageHandler.getAscendingCountdowns();
        else
            countdowns = StorageHandler.getDescendingCountdowns();

        fp.getChildren().clear();
        this.cdPanes.clear();
        for (Countdown c : countdowns) {
            CountdownPane countdownPane = new CountdownPane(c, now);
            fp.getChildren().add(countdownPane);
            this.cdPanes.add(countdownPane);
        }
        addPaddingForAlignment();
    }

    private void addPaddingForAlignment() {
        this.paddingsInUse.forEach(
            padding -> CountdownPaneView.getInstance().getChildren().remove(padding));
        if (this.cdPanes.isEmpty())
            return;
        double cdWidth = CountdownPane.WIDTH;
        double cdHeight = CountdownPane.HEIGHT;
        double width = this.fp.getWidth();
        int columns = (int) Math.floor(width / cdWidth);
        int panesOnLast = this.cdPanes.size() % columns;
        int remainder = (panesOnLast > 0) ? columns - panesOnLast : 0;
        for (int i = 0; i < remainder; i++) {
            Region padding = new Region();
            padding.setMinSize(cdWidth, cdHeight);
            padding.setMaxSize(cdWidth, cdHeight);
            // padding.setBackground(Colour.createBG(Color.PINK, 0, 0));
            padding.setVisible(false);
            this.fp.getChildren().add(padding);
            this.paddingsInUse.add(padding);
        }
    }

    public LinkedHashSet<CountdownPane> getAllSelected() {
        LinkedHashSet<CountdownPane> selected = new LinkedHashSet<>();
        for (CountdownPane pane : this.cdPanes) {
            if (pane.isSelected())
                selected.add(pane);
        }
        return selected;
    }

    public void addSelected(CountdownPane pane) {
        this.cdPanes.add(pane);
        CountdownPaneControls controls = CountdownPaneControls.getInstance();
        if (this.cdPanes.size() == 1) {
            controls.setMode(ControlMode.SINGLE_SELECT);
        } else if (this.cdPanes.size() > 1) {
            controls.setMode(ControlMode.MULTI_SELECT);
        }
    }

    public void removeSelected(CountdownPane pane) {
        this.cdPanes.remove(pane);
        CountdownPaneControls controls = CountdownPaneControls.getInstance();
        if (this.cdPanes.size() == 0) {
            controls.setMode(ControlMode.NO_SELECT);
        } else if (this.cdPanes.size() == 1) {
            controls.setMode(ControlMode.SINGLE_SELECT);
        }
    }

    public DisplayOrder getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(DisplayOrder displayOrder) {
        this.displayOrder = displayOrder;
    }
}
