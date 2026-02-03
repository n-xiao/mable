package code.frontend.libs.katlaf.dragndrop;

import javafx.scene.layout.Region;

/**
 * An abstract representation of a UI component that can be dragged (and dropped)
 * by the user.
 */
public interface DragStarter<T> {
    /**
     * This will be called by {@link DragDropOverlay} when
     * a drag action starts. This method does not need to
     * do anything.
     */
    void onDragStart();

    /**
     * Gets the data that is being transferred.
     */
    T getData();

    /**
     * This is the preview that will be shown when this {@link DragStarter}
     * is being dragged across the screen. While null can be returned,
     * it is discouraged as users like to see what they are dragging.
     *
     * @return a Region which is a visual representation of the component
     * that is being dragged
     */
    Region getRepresentation();
}
