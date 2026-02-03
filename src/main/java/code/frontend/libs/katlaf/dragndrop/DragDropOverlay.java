package code.frontend.libs.katlaf.dragndrop;

import javafx.scene.layout.Region;

/**
 * This is an unmanaged Region which implements drag and drop
 * visuals, as well as facilitates some communication between
 * {@link DragStarter} instances and {@link DragStopper}
 * instances.
 */
public class DragDropOverlay extends Region {
    private static DragDropOverlay activeOverlay = null; // max of one instance should exist
    private final DragStarter<?> dragStarter;

    public DragDropOverlay(DragStarter<?> dragStarter) {
        DragDropOverlay.activeOverlay = this;
        this.dragStarter = dragStarter;
    }

    /**
     * A method which compares the class of the data that is being
     * transferred by the current drag and drop process with a provided
     * class.
     *
     * @return true if the class of the data is equal to the provided class.
     */
    protected static <E> boolean checkMatchingTypes(Class<? extends E> c) {
        if (activeOverlay.dragStarter == null || activeOverlay.dragStarter.getData() == null)
            return false; // if null checks fail
        return activeOverlay.dragStarter.getData().getClass().equals(c);
    }
}
