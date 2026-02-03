package code.frontend.libs.katlaf.dragndrop;

public interface DragStopper<T> {
    void onDragStop(T data);
    void onDragHover();

    default boolean verifyOverlay() {
        return DragDropOverlay.getDragStarter().getData() instanceof T;
    }
}
