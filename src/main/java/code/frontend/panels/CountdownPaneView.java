package code.frontend.panels;

import java.time.LocalDate;

import code.backend.Countdown;
import code.backend.StorageHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;

enum DisplayOrder
{
    ASCENDING,
    DESCENDING
}

/**
 * This is the scrollable Pane that is responsible for displaying {@link CountdownPane}.
 * An instance of this class should be added to a {@link ContentView} instance.
 */
public class CountdownPaneView extends ScrollPane
{
    private final double GAP_BETWEEN = 8;
    private FlowPane fp;

    private static CountdownPaneView cpv = null;

    private CountdownPaneView()
    {
        this.fp = new FlowPane();
        // fp.prefWidthProperty().bind(this.widthProperty());
        fp.prefWrapLengthProperty().bind(this.widthProperty());
        fp.setHgap(GAP_BETWEEN);
        fp.setVgap(GAP_BETWEEN);
        this.setFitToWidth(true);
        this.setHbarPolicy(ScrollBarPolicy.NEVER);
        this.setVbarPolicy(ScrollBarPolicy.NEVER);
        this.getChildren().add(fp);
    }

    public static CountdownPaneView getInstance()
    {
        cpv = (cpv == null) ? new CountdownPaneView() : cpv;
        return cpv;
    }

    public void repopulate(LocalDate now, DisplayOrder order)
    {
        Countdown[] countdowns;
        // this if-else is ok for now since there's only two DisplayOrders rn
        if (order.equals(DisplayOrder.ASCENDING))
            countdowns = StorageHandler.getAscendingCountdowns();
        else
            countdowns = StorageHandler.getDescendingCountdowns();

        fp.getChildren().clear();
        for (Countdown c : countdowns)
            {
                CountdownPane countdownPane = new CountdownPane(c, now);
                fp.getChildren().add(countdownPane);
            }
    }
}
