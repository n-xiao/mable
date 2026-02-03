package code.frontend.libs.katlaf.tables;

import code.frontend.libs.katlaf.graphics.BorderedRegion;

public abstract class SimpleTableMember extends BorderedRegion {
    public SimpleTableMember(final double width, final double height) {
        super(2.2, 0.19, 0.42);
        this.setMinWidth(width);
        this.setMaxWidth(width);
        this.setMinHeight(height);
        this.setMaxHeight(height);
        // init listeners
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/
}
