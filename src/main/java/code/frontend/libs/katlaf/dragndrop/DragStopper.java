package code.frontend.libs.katlaf.dragndrop;

public interface DragStopper {
    void onDragStop();
    void onDragHover();
    Class<?> getExpectedType();

    default boolean isAccepting() {
        return DragDropOverlay.checkMatchingTypes(getExpectedType());
    }
}
