package code.frontend.libs.katlaf.graphics;

import code.frontend.libs.katlaf.menus.RightClickMenu;

public abstract class DraggableBorderedRegion extends BorderedRegion {
    public DraggableBorderedRegion(double thickness, double messiness, double cornerRadii) {
        super(thickness, messiness, cornerRadii);
    }

    /*


     BEHAVIOUR
    -------------------------------------------------------------------------------------*/

    private void initDragDetection() {
        this.setOnDragDetected((event) -> {
            RightClickMenu.despawnAll();
            this.startFullDrag();
            // TODO
        });
    }

    /*


     PROTECTED API
    -------------------------------------------------------------------------------------*/

    protected abstract void onDragStart();
    protected abstract void onDragStop();
    protected abstract void onDragSuccess();
}
