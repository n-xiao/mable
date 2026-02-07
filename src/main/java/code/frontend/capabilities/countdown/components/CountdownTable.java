package code.frontend.capabilities.countdown.components;

import code.backend.data.Countdown;
import code.frontend.capabilities.concurrency.Updatable;
import code.frontend.capabilities.concurrency.Watchdog;
import code.frontend.libs.katlaf.dragndrop.DragStartRegion;
import code.frontend.libs.katlaf.graphics.MableBorder;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import code.frontend.libs.katlaf.tables.SimpleTable;
import code.frontend.libs.katlaf.tables.SimpleTableMember;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Rotate;

public class CountdownTable extends SimpleTable {
    public CountdownTable() {
        this.setHgap(35);
        this.setVgap(15);
    }

    /*


     COMPOSITIONS
    -------------------------------------------------------------------------------------*/

    private class Member extends SimpleTableMember implements Updatable {
        private final CountdownPane countdownPane;
        Member(final Countdown countdown) {
            super(CountdownPane.WIDTH, CountdownPane.HEIGHT);
            this.countdownPane = new CountdownPane(countdown, LocalDate.now());
            this.getChildren().addAll(this.countdownPane, new DragDetector());
            Watchdog.watch(this);
        }

        @Override
        protected void onSelected() {
            countdownPane.applySelectStyle();
        }

        @Override
        protected void onDeselected() {
            countdownPane.applyDeselectStyle(false);
        }

        @Override
        protected void onRightClicked(ArrayList<SimpleTableMember> selectedMembers) {
            // TODO Auto-generated method stub
        }

        @Override
        public int compareTo(SimpleTableMember o) {
            if (o instanceof Member) {
                final LocalDate now = LocalDate.now();
                final Member other = (Member) o;
                return countdownPane.getCountdown().daysUntilDue(now)
                    - other.countdownPane.getCountdown().daysUntilDue(now);
            }
            throw new IllegalArgumentException(
                "can't compare something that isn't a member of this CountdownTable");
        }

        @Override
        public void update() {
            this.countdownPane.refreshContent();
        }

        private class DragDetector extends DragStartRegion<Countdown> {
            DragDetector() {
                super();
                this.resize(CountdownPane.WIDTH, CountdownPane.HEIGHT);
            }
            @Override
            protected void onDragStart() {
                Member.this.setOpacity(0.65);
            }

            @Override
            protected Region getRepresentation() {
                final int width = 220;
                final int height = 80;
                final int size = CountdownTable.this.getSelectedMembers().size();

                final Pane container = new Pane();
                container.resize(400, 400);
                container.setBackground(null);

                for (int i = 0; i < size && i < 3; i++) {
                    final Member member = (Member) CountdownTable.this.getSelectedMembers().get(i);
                    final MableBorder border = new MableBorder(2.4, 0.2, 0.42);
                    border.setStrokeColour(member.countdownPane.getBorderColour());

                    final Pane pane = new Pane();
                    MableBorder.applyToPane(pane, border);
                    pane.setBackground(
                        RiceHandler.createBG(RiceHandler.getColour("selected"), 14, 2));
                    pane.setManaged(false);
                    pane.resizeRelocate(0, 0, width, height);
                    pane.getTransforms().add(new Rotate((i + 1) * 5, 0, 0));
                    container.getChildren().add(pane);
                }

                return container;
            }

            @Override
            protected Countdown getData() {
                return Member.this.countdownPane.getCountdown();
            }

            @Override
            protected void cleanupOnDragEnd() {
                Member.this.setOpacity(1);
            }
        }
    }
}
