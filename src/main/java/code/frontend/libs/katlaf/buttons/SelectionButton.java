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

import code.frontend.libs.katlaf.graphics.MableBorder;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * A BorderedLabelledButtonFace that reacts to user mouse actions. This class holds
 * references to three colours: idle, hover and active. All selection buttons are
 * first coloured using idle. On mouse hover, the colour of the button will be
 * changed to the hover colour. When the user clicks on this button, the
 * active colour will be used.
 *
 * @see BorderedLabelledButtonFace
 */
public class SelectionButton extends BorderedLabelledButtonFace {
    private Color idle;
    private Color hover; // colour of button when mouse hovers over
    private Color active; // colour of button when it has been clicked or selected

    public SelectionButton(final MableBorder mableBorder) {
        super(mableBorder);
        // set default values
        this.idle = RiceHandler.getColour("dullgrey");
        this.hover = RiceHandler.getColour("lightgrey");
        this.active = RiceHandler.getColour("white");
        this.setColour(this.idle);
    }

    /*


     PROTECTED API
    -------------------------------------------------------------------------------------*/

    protected final void setIdleColour(final Color idle) {
        this.idle = idle;
    }

    protected final void setHoverColour(final Color hover) {
        this.hover = hover;
    }

    protected final void setActiveColour(final Color active) {
        this.active = active;
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    /**
     * Implements styling when clicked. When overriding this method, please
     * remember to call the superclass method or the styling will not be applied.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void onMousePressed(MouseEvent event) {
        if (this.isEnabled() && this.isToggled()) {
            this.setColour(this.active);
        } else if (this.isEnabled() && this.isHover()) {
            this.setColour(this.hover);
        } else if (this.isEnabled()) {
            this.setColour(this.idle);
        } else {
            this.setColour(DISABLED_COLOUR);
        }
    }

    @Override
    public final void onMouseEntered(MouseEvent event) {
        if (this.isEnabled() && !this.isToggled()) {
            this.setColour(this.hover);
        }
    }

    @Override
    public final void onMouseExited(MouseEvent event) {
        if (this.isEnabled() && !this.isToggled()) {
            this.setColour(this.idle);
        }
    }

    @Override
    public final void onMouseReleased(MouseEvent event) {
        // does nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setToggle(final boolean toggled) {
        if (toggled) {
            this.setColour(this.active);
        } else {
            this.setColour(this.idle);
        }
        super.setToggle(toggled);
    }
}
