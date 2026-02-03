package code.frontend.libs.katlaf.dragndrop;

public interface DragStopper {
    /**
     * This method is called when the mouse is released
     * whilst hovering over this {@link DragStopper},
     * finishing the drag process. This method is
     * a convenience method, and could be implemented
     * to do nothing.
     */
    void onDragStop();

    /**
     * This method is called whenever the mouse, while
     * dragging with an active {@link DragDropOverlay},
     * enters this {@link DragStopper}. This is a
     * convenience method which can be used to
     * implement special highlighting or styling to
     * provide visual indications.
     *
     * Tip: you an use the isAccepting() method to check
     * if the current drag and drop process is transferring
     * data of relevance to this {@link DragStopper}.
     */
    void onDragEnter();

    void onDragExit();

    /**
     * It is the implementor's responsibility to specify
     * the expected type of this {@link DragStopper}.
     * Do so by implementing this method.
     * This should never return null. Never, ever, ever.
     */
    Class<?> getExpectedType();

    /**
     * A check to see if this {@link DragStopper} can accept
     * the data that is currently being dragged.
     *
     * @return true if the data that is being dragged is of the
     * expected type of this {@link DragStopper}, false otherwise.
     */
    default boolean isAccepting() {
        return DragDropOverlay.checkMatchingTypes(getExpectedType());
    }
}
