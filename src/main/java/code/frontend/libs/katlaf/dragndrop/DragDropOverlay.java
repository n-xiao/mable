package code.frontend.libs.katlaf.dragndrop;

import javafx.scene.layout.Region;

/**
 * This is an unmanaged Region which implements drag and drop
 * visuals, as well as facilitates some communication between
 * {@link DragStarter} instances and {@link DragStopper}
 * instances.
 */
public class DragDropOverlay extends Region {
    private static DragDropOverlay activeOverlay = null;

    public DragDropOverlay(DragStarter<?> dragStarter) {
        DragDropOverlay.activeOverlay = this;
        this.dragStarter = dragStarter;
    }

    private final DragStarter<?> dragStarter;
    protected static DragStarter<?> getDragStarter() {
        return activeOverlay == null ? null : activeOverlay.dragStarter;
    }
}
