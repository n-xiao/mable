/*
    Copyright (C) 2026 Nicholas Siow <nxiao.dev@gmail.com>
    DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package code.frontend.libs.katlaf.buttons;

import code.frontend.libs.katlaf.graphics.BorderedRegion;
import code.frontend.libs.katlaf.graphics.MableBorder;

/**
 * This is a child class of LabelledButtonLogic, but with a MableBorder as a child node.
 *
 * @see LabelledButtonFace
 */
public abstract class BorderedButtonFace extends LabelledButtonFace {
    private final BorderedRegion borderRegion;

    public BorderedButtonFace(
        final double thickness, final double messiness, final double cornerRadii) {
        this.borderRegion = new BorderedRegion(thickness, messiness, cornerRadii);
        this.borderRegion.prefWidthProperty().bind(this.widthProperty());
        this.borderRegion.prefHeightProperty().bind(this.heightProperty());
        this.borderRegion.setMouseTransparent(true);
        this.getChildren().add(this.borderRegion);
    }

    /*


     PROTECTED
    -------------------------------------------------------------------------------------*/

    /**
     * Gets the MableBorder associated with this button.
     */
    protected final MableBorder getMableBorder() {
        return this.borderRegion.getCustomBorder();
    }
}
