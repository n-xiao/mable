package code.frontend.panels;

import code.backend.Countdown;
import code.backend.StorageHandler;
import code.frontend.misc.Vals.Colour;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * This is the scrollable Pane that is responsible for displaying {@link CountdownPane}.
 * An instance of this class should be added to a {@link Content} instance.
 */
public class CountdownPaneView extends ScrollPane {
    enum DisplayOrder { ASCENDING, DESCENDING }
    private final double GAP_BETWEEN = 8;
    private FlowPane fp;
    private LinkedHashSet<CountdownPane> cdPanes;
    private DisplayOrder displayOrder;

    private static CountdownPaneView cpv = null;

    private CountdownPaneView() {
        this.displayOrder = DisplayOrder.DESCENDING;
        this.cdPanes = new LinkedHashSet<>();
        this.fp = new FlowPane();
        this.fp.prefWrapLengthProperty().bind(this.widthProperty());
        // the -2 below is needed to correct a small offset when at minHeight
        this.fp.minHeightProperty().bind(this.heightProperty().add(-2));
        this.fp.setMaxHeight(Double.MAX_VALUE);
        this.fp.setBackground(Colour.createBG(Colour.BACKGROUND, 0, 0));
        this.fp.setHgap(GAP_BETWEEN);
        this.fp.setVgap(GAP_BETWEEN);
        this.setBackground(null);
        this.setFitToWidth(true);
        this.setHbarPolicy(ScrollBarPolicy.NEVER);
        this.setVbarPolicy(ScrollBarPolicy.NEVER);
        this.setContent(this.fp);
    }

    public static CountdownPaneView getInstance() {
        cpv = (cpv == null) ? new CountdownPaneView() : cpv;
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
    }

    public CountdownPane[] getAllSelected() {
        HashSet<CountdownPane> selected = new HashSet<>();
        for (CountdownPane pane : this.cdPanes) {
            if (pane.isSelected())
                selected.add(pane);
        }
        return (CountdownPane[]) selected.toArray();
    }

    public DisplayOrder getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(DisplayOrder displayOrder) {
        this.displayOrder = displayOrder;
    }
}
